package br.com.marketflow.produto.dto;

import br.com.marketflow.produto.entity.Produto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        boolean ativo,
        Long categoriaId,
        String categoriaNome
) {

    public static ProdutoResponseDTO from(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.isAtivo(),
                produto.getCategoria().getId(),
                produto.getCategoria().getNome()
        );
    }

}
