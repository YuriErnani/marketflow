package br.com.marketflow.usuario.dto;

import br.com.marketflow.infra.validation.TamanhoMaximoEmBytes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlterarSenhaRequestDTO(
        @NotBlank
        @TamanhoMaximoEmBytes(72)
        String senhaAtual,

        @NotBlank
        @Size(min = 8)
        @TamanhoMaximoEmBytes(72)
        String novaSenha
) {}
