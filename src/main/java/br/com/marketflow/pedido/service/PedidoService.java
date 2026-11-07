package br.com.marketflow.pedido.service;

import br.com.marketflow.carrinho.entity.ItemCarrinho;
import br.com.marketflow.carrinho.exception.CarrinhoVazioException;
import br.com.marketflow.carrinho.service.CarrinhoService;
import br.com.marketflow.estoque.service.EstoqueService;
import br.com.marketflow.infra.dto.PaginaResponseDTO;
import br.com.marketflow.pedido.dto.FinalizarPedidoRequestDTO;
import br.com.marketflow.pedido.dto.PedidoResponseDTO;
import br.com.marketflow.pedido.dto.PedidoResumoResponseDTO;
import br.com.marketflow.pedido.entity.Pedido;
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

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoService carrinhoService;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

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

}