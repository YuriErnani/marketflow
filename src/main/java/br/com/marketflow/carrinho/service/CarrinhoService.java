package br.com.marketflow.carrinho.service;

import br.com.marketflow.carrinho.dto.AdicionarItemCarrinhoRequestDTO;
import br.com.marketflow.carrinho.dto.CarrinhoResponseDTO;
import br.com.marketflow.carrinho.dto.ItemCarrinhoResponseDTO;
import br.com.marketflow.carrinho.entity.ItemCarrinho;
import br.com.marketflow.carrinho.exception.ItemCarrinhoNaoEncontradoException;
import br.com.marketflow.carrinho.repository.ItemCarrinhoRepository;
import br.com.marketflow.estoque.exception.EstoqueInsuficienteException;
import br.com.marketflow.estoque.service.EstoqueService;
import br.com.marketflow.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final ItemCarrinhoRepository itemRepository;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    @Transactional(readOnly = true)
    public CarrinhoResponseDTO consultar(Long usuarioId) {
        return montarCarrinho(itemRepository.findByUsuarioIdOrderByIdAsc(usuarioId));
    }

    @Transactional
    public CarrinhoResponseDTO adicionarItem(Long usuarioId, AdicionarItemCarrinhoRequestDTO request) {
        var produto = produtoService.buscarEntidadeAtiva(request.produtoId());

        var itemExistente = itemRepository
                .findByUsuarioIdAndProdutoId(usuarioId, produto.getId())
                .orElse(null);

        var quantidadeAtual = itemExistente == null ? 0 : itemExistente.getQuantidade();
        var novaQuantidade = quantidadeAtual + request.quantidade();

        validarEstoque(produto.getId(), novaQuantidade);

        if (itemExistente == null)
            itemRepository.save(new ItemCarrinho(usuarioId, produto.getId(), novaQuantidade));
        else
            itemExistente.alterarQuantidade(novaQuantidade);

        return montarCarrinho(itemRepository.findByUsuarioIdOrderByIdAsc(usuarioId));
    }

    @Transactional
    public CarrinhoResponseDTO alterarQuantidade(Long usuarioId, Long produtoId, int quantidade) {
        var item = buscarItem(usuarioId, produtoId);

        produtoService.buscarEntidadeAtiva(produtoId);
        validarEstoque(produtoId, quantidade);

        item.alterarQuantidade(quantidade);

        return montarCarrinho(itemRepository.findByUsuarioIdOrderByIdAsc(usuarioId));
    }

    @Transactional
    public void removerItem(Long usuarioId, Long produtoId) {
        itemRepository.delete(buscarItem(usuarioId, produtoId));
    }

    private void validarEstoque(Long produtoId, int quantidade) {
        if (estoqueService.quantidadeDisponivel(produtoId) < quantidade)
            throw new EstoqueInsuficienteException();
    }

    private ItemCarrinho buscarItem(Long usuarioId, Long produtoId) {
        return itemRepository.findByUsuarioIdAndProdutoId(usuarioId, produtoId)
                .orElseThrow(ItemCarrinhoNaoEncontradoException::new);
    }

    private CarrinhoResponseDTO montarCarrinho(List<ItemCarrinho> itens) {
        if (itens.isEmpty())
            return new CarrinhoResponseDTO(List.of(), BigDecimal.ZERO, false);

        var produtoIds = itens
                .stream()
                .map(ItemCarrinho::getProdutoId)
                .toList();

        var produtos = produtoService.buscarEntidadesPorIds(produtoIds);
        var estoques = estoqueService.quantidadesDisponiveis(produtoIds);

        var respostas = itens
                .stream()
                .map(item -> {
                    var produto = produtos.get(item.getProdutoId());

                    var subtotal = produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));

                    var disponivel = produto.isAtivo() && estoques.getOrDefault(item.getProdutoId(), 0) >= item.getQuantidade();

                    return new ItemCarrinhoResponseDTO(
                            produto.getId(),
                            produto.getNome(),
                            produto.getPreco(),
                            item.getQuantidade(),
                            subtotal,
                            disponivel);
                })
                .toList();

        var total = respostas
                .stream()
                .map(ItemCarrinhoResponseDTO::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var podeFinalizar = respostas
                .stream()
                .allMatch(ItemCarrinhoResponseDTO::disponivel);

        return new CarrinhoResponseDTO(respostas, total, podeFinalizar);
    }

}
