package br.com.marketflow.pagamento.dto;

import br.com.marketflow.pagamento.entity.ResultadoPagamentoSimulado;
import jakarta.validation.constraints.NotNull;

public record RegistrarPagamentoRequestDTO(
        @NotNull
        ResultadoPagamentoSimulado resultadoSimulado
) {}
