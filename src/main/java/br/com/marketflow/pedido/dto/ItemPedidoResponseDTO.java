package br.com.marketflow.pedido.dto;

import br.com.marketflow.pedido.entity.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Long produtoId,
        String nomeProduto,
        BigDecimal precoUnitario,
        int quantidade,
        BigDecimal subtotal
) {

    public static ItemPedidoResponseDTO from(ItemPedido item) {
        return new ItemPedidoResponseDTO(
                item.getProdutoId(),
                item.getNomeProduto(),
                item.getPrecoUnitario(),
                item.getQuantidade(),
                item.getSubtotal()
        );
    }

}
