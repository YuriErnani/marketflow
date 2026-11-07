package br.com.marketflow.pedido.dto;

import br.com.marketflow.pedido.entity.Pedido;
import br.com.marketflow.pedido.entity.StatusPedido;

import java.math.BigDecimal;
import java.time.Instant;

public record PedidoResumoResponseDTO(
        Long id,
        Long usuarioId,
        StatusPedido status,
        BigDecimal total,
        Instant criadoEm
) {

    public static PedidoResumoResponseDTO from(Pedido pedido) {
        return new PedidoResumoResponseDTO(
                pedido.getId(),
                pedido.getUsuarioId(),
                pedido.getStatus(),
                pedido.getTotal(),
                pedido.getCriadoEm()
        );
    }

}
