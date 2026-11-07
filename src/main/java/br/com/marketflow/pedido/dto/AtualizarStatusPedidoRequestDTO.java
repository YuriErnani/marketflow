package br.com.marketflow.pedido.dto;

import br.com.marketflow.pedido.entity.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoRequestDTO(
        @NotNull
        StatusPedido status
) {}
