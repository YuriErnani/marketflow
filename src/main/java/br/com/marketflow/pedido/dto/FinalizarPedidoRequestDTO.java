package br.com.marketflow.pedido.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FinalizarPedidoRequestDTO(
        @NotBlank
        @Size(max = 500)
        String enderecoEntrega
) {}
