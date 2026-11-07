package br.com.marketflow.pagamento.dto;

import br.com.marketflow.pagamento.entity.Pagamento;
import br.com.marketflow.pagamento.entity.StatusPagamento;

import java.math.BigDecimal;
import java.time.Instant;

public record PagamentoResponseDTO(
        Long id,
        Long pedidoId,
        StatusPagamento status,
        BigDecimal valor,
        Instant criadoEm
) {

    public static PagamentoResponseDTO from(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getStatus(),
                pagamento.getValor(),
                pagamento.getCriadoEm()
        );
    }

}
