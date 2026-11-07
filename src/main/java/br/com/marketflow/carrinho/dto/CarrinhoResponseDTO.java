package br.com.marketflow.carrinho.dto;

import java.math.BigDecimal;
import java.util.List;

public record CarrinhoResponseDTO(
        List<ItemCarrinhoResponseDTO> itens,
        BigDecimal total,
        boolean podeFinalizar
) {}
