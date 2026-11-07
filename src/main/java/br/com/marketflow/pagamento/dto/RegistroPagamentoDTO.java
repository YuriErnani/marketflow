package br.com.marketflow.pagamento.dto;

public record RegistroPagamentoDTO(
        PagamentoResponseDTO pagamento,
        boolean novo
) {}
