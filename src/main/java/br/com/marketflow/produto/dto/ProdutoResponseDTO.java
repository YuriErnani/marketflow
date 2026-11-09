package br.com.marketflow.produto.dto;

import br.com.marketflow.produto.entity.Produto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        boolean ativo,
        boolean disponivel,
        Long categoriaId,
        String categoriaNome
) {

    public static ProdutoResponseDTO from(Produto produto, int quantidadeDisponivel) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.isAtivo(),
                produto.isAtivo() && quantidadeDisponivel > 0,
                produto.getCategoria().getId(),
                produto.getCategoria().getNome()
        );
    }

}
