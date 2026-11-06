package br.com.marketflow.produto.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank
        @Size(max = 120)
        String nome,

        @Size(max = 1000)
        String descricao,

        @NotNull
        @Positive
        @Digits(integer = 8, fraction = 2)
        BigDecimal preco,

        @NotNull
        Long categoriaId
) {}
