package br.com.marketflow.estoque.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EntradaEstoqueRequestDTO(
        @NotNull
        @Positive
        @Max(10000)
        Integer quantidade
) {}
