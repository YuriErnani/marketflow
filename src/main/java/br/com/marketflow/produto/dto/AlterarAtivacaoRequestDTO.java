package br.com.marketflow.produto.dto;

import jakarta.validation.constraints.NotNull;

public record AlterarAtivacaoRequestDTO(
        @NotNull
        Boolean ativo
) {}
