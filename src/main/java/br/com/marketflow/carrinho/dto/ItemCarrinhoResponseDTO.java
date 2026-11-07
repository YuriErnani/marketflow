package br.com.marketflow.carrinho.dto;

import java.math.BigDecimal;

public record ItemCarrinhoResponseDTO(
        Long produtoId,
        String nome,
        BigDecimal precoUnitario,
        int quantidade,
        BigDecimal subtotal,
        boolean disponivel
) {}
