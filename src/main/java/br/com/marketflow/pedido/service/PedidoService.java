package br.com.marketflow.pedido.service;

import br.com.marketflow.carrinho.entity.ItemCarrinho;
import br.com.marketflow.carrinho.exception.CarrinhoVazioException;
import br.com.marketflow.carrinho.service.CarrinhoService;
import br.com.marketflow.pedido.exception.TransicaoStatusInvalidaException;
import br.com.marketflow.estoque.service.EstoqueService;
import br.com.marketflow.infra.dto.PaginaResponseDTO;
import br.com.marketflow.pagamento.dto.PagamentoResponseDTO;
import br.com.marketflow.pagamento.dto.RegistroPagamentoDTO;
import br.com.marketflow.pagamento.entity.ResultadoPagamentoSimulado;
import br.com.marketflow.pagamento.entity.StatusPagamento;
import br.com.marketflow.pagamento.exception.ChaveIdempotenciaEmUsoException;
import br.com.marketflow.pagamento.exception.ChaveIdempotenciaInvalidaException;
import br.com.marketflow.pagamento.service.PagamentoService;
import br.com.marketflow.pedido.dto.FinalizarPedidoRequestDTO;
import br.com.marketflow.pedido.dto.PedidoResponseDTO;
import br.com.marketflow.pedido.dto.PedidoResumoResponseDTO;
import br.com.marketflow.pedido.entity.ItemPedido;
import br.com.marketflow.pedido.entity.Pedido;
import br.com.marketflow.pedido.entity.StatusPedido;
import br.com.marketflow.pedido.exception.PedidoNaoEncontradoException;
import br.com.marketflow.pedido.exception.ProdutoIndisponivelException;
import br.com.marketflow.pedido.repository.PedidoRepository;
import br.com.marketflow.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoService carrinhoService;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final PagamentoService pagamentoService;

    @Transactional(readOnly = true)
    public PaginaResponseDTO<PedidoResumoResponseDTO> listarPedidos(Long usuarioId, boolean admin, Pageable pageable) {
        var paginaOrdenada = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "criadoEm", "id"));

        var pagina = admin ? pedidoRepository.findAll(paginaOrdenada) : pedidoRepository.findByUsuarioId(usuarioId, paginaOrdenada);

        return PaginaResponseDTO.from(pagina.map(PedidoResumoResponseDTO::from));
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPedidoPorId(Long pedidoId, Long usuarioId, boolean admin) {
        var pedido = pedidoRepository.findComItensById(pedidoId)
                .orElseThrow(PedidoNaoEncontradoException::new);

        if (!admin && !pedido.getUsuarioId().equals(usuarioId))
            throw new PedidoNaoEncontradoException();

        return PedidoResponseDTO.from(pedido);
    }

    @Transactional
    public PedidoResponseDTO finalizarCompra(Long usuarioId, FinalizarPedidoRequestDTO request) {
        var itensCarrinho = carrinhoService.listarItens(usuarioId)
                .stream()
                .sorted(Comparator.comparing(ItemCarrinho::getProdutoId))
                .toList();

        if (itensCarrinho.isEmpty())
            throw new CarrinhoVazioException();

        var produtoIds = itensCarrinho
                .stream()
                .map(ItemCarrinho::getProdutoId)
                .toList();

        var produtos = produtoService.buscarEntidadesPorIds(produtoIds);

        var pedido = new Pedido(usuarioId, request.enderecoEntrega().trim());

        for (var item : itensCarrinho) {
            var produto = produtos.get(item.getProdutoId());

            if (!produto.isAtivo())
                throw new ProdutoIndisponivelException(produto.getNome());

            pedido.adicionarItem(produto.getId(), produto.getNome(), produto.getPreco(), item.getQuantidade());
        }

        pedidoRepository.save(pedido);

        for (var item : itensCarrinho)
            estoqueService.reservar(item.getProdutoId(), item.getQuantidade(), pedido.getId());

        carrinhoService.esvaziarCarrinho(usuarioId);

        return PedidoResponseDTO.from(pedido);
    }

    @Transactional
    public PedidoResponseDTO cancelar(Long pedidoId, Long usuarioId, boolean admin) {
        var pedido = buscarParaAtualizacao(pedidoId, usuarioId, admin);

        if (!admin && pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO)
            throw new TransicaoStatusInvalidaException("O cliente só pode cancelar pedidos que aguardam pagamento.");

        cancelarEAtualizarEstoque(pedido);

        return PedidoResponseDTO.from(pedido);
    }

    @Transactional(readOnly = true)
    public List<Long> buscarIdsParaExpirar(Instant limite) {
        return pedidoRepository.buscarIdsCriadosAntesDe(StatusPedido.AGUARDANDO_PAGAMENTO, limite);
    }

    @Transactional
    public boolean expirar(Long pedidoId) {
        var pedido = pedidoRepository.buscarParaAtualizacao(pedidoId)
                .orElseThrow(PedidoNaoEncontradoException::new);

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO)
            return false;

        cancelarEAtualizarEstoque(pedido);

        return true;
    }

    private void cancelarEAtualizarEstoque(Pedido pedido) {
        var statusAnterior = pedido.getStatus();

        pedido.cancelarPedido();

        var itens = pedido.getItens().stream()
                .sorted(Comparator.comparing(ItemPedido::getProdutoId))
                .toList();

        for (var item : itens) {
            if (statusAnterior == StatusPedido.AGUARDANDO_PAGAMENTO)
                estoqueService.liberarReserva(item.getProdutoId(), item.getQuantidade(), pedido.getId());
            else
                estoqueService.devolver(item.getProdutoId(), item.getQuantidade(), pedido.getId());
        }

        if (statusAnterior != StatusPedido.AGUARDANDO_PAGAMENTO)
            pagamentoService.estornarAprovado(pedido.getId());
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long pedidoId, StatusPedido novoStatus) {
        var pedido = pedidoRepository.buscarParaAtualizacao(pedidoId)
                .orElseThrow(PedidoNaoEncontradoException::new);

        pedido.avancarStatus(novoStatus);

        return PedidoResponseDTO.from(pedido);
    }

    private Pedido buscarParaAtualizacao(Long pedidoId, Long usuarioId, boolean admin) {
        var pedido = pedidoRepository.buscarParaAtualizacao(pedidoId)
                .orElseThrow(PedidoNaoEncontradoException::new);

        if (!admin && !pedido.getUsuarioId().equals(usuarioId))
            throw new PedidoNaoEncontradoException();

        return pedido;
    }

    @Transactional
    public RegistroPagamentoDTO registrarPagamento(Long pedidoId, Long usuarioId, String chave, ResultadoPagamentoSimulado resultado) {
        if (chave.isBlank() || chave.length() > 100)
            throw new ChaveIdempotenciaInvalidaException();

        var pedido = buscarParaAtualizacao(pedidoId, usuarioId, false);

        var existente = pagamentoService.buscarPorChave(chave);

        if (existente.isPresent()) {
            var pagamento = existente.get();

            if (!pagamento.getPedidoId().equals(pedidoId))
                throw new ChaveIdempotenciaEmUsoException();

            return new RegistroPagamentoDTO(PagamentoResponseDTO.from(pagamento), false);
        }

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO)
            throw new TransicaoStatusInvalidaException("Este pedido não está aguardando pagamento.");

        var status = resultado == ResultadoPagamentoSimulado.APROVADO ? StatusPagamento.APROVADO : StatusPagamento.RECUSADO;

        var pagamento = pagamentoService.registrar(pedido.getId(), chave, pedido.getTotal(), status);

        if (status == StatusPagamento.APROVADO) {
            pedido.aprovarPagamento();

            var itens = pedido.getItens()
                    .stream()
                    .sorted(Comparator.comparing(ItemPedido::getProdutoId))
                    .toList();

            for (var item : itens)
                estoqueService.baixarReserva(item.getProdutoId(), item.getQuantidade(), pedido.getId());
        }

        return new RegistroPagamentoDTO(PagamentoResponseDTO.from(pagamento), true);
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarPagamentos(Long pedidoId, Long usuarioId, boolean admin) {
        var pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(PedidoNaoEncontradoException::new);

        if (!admin && !pedido.getUsuarioId().equals(usuarioId))
            throw new PedidoNaoEncontradoException();

        return pagamentoService.listarPorPedido(pedidoId);
    }

}
