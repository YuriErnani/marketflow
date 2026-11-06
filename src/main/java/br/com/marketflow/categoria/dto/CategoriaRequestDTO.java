package br.com.marketflow.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(
        @NotBlank
        @Size(max = 80)
        String nome,

        @Size(max = 255)
        String descricao
) {}
