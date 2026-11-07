package br.com.marketflow.carrinho.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AlterarQuantidadeItemRequestDTO(
        @NotNull
        @Positive
        @Max(100)
        Integer quantidade
) {}
