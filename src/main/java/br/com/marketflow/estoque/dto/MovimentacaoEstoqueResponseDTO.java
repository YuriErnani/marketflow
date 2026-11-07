package br.com.marketflow.estoque.dto;

import br.com.marketflow.estoque.entity.MovimentacaoEstoque;
import br.com.marketflow.estoque.entity.TipoMovimentacao;

import java.time.Instant;

public record MovimentacaoEstoqueResponseDTO(
        Long id,
        TipoMovimentacao tipo,
        int quantidade,
        Long pedidoId,
        Instant criadoEm
) {

    public static MovimentacaoEstoqueResponseDTO from(MovimentacaoEstoque movimentacao) {
        return new MovimentacaoEstoqueResponseDTO(
                movimentacao.getId(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getPedidoId(),
                movimentacao.getCriadoEm()
        );
    }

}
