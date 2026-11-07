package br.com.marketflow.pedido.dto;

import br.com.marketflow.pedido.entity.Pedido;
import br.com.marketflow.pedido.entity.StatusPedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        Long usuarioId,
        StatusPedido status,
        BigDecimal total,
        String enderecoEntrega,
        Instant criadoEm,
        List<ItemPedidoResponseDTO> itens
) {

    public static PedidoResponseDTO from(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getUsuarioId(),
                pedido.getStatus(),
                pedido.getTotal(),
                pedido.getEnderecoEntrega(),
                pedido.getCriadoEm(),
                pedido.getItens()
                        .stream()
                        .map(ItemPedidoResponseDTO::from)
                        .toList()
        );
    }

}
