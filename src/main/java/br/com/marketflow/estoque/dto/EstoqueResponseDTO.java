package br.com.marketflow.estoque.dto;

import br.com.marketflow.estoque.entity.Estoque;

public record EstoqueResponseDTO(
        Long produtoId,
        int quantidadeDisponivel,
        int quantidadeReservada
) {

    public static EstoqueResponseDTO from(Estoque estoque) {
        return new EstoqueResponseDTO(
                estoque.getProdutoId(),
                estoque.getQuantidadeDisponivel(),
                estoque.getQuantidadeReservada()
        );
    }

}
