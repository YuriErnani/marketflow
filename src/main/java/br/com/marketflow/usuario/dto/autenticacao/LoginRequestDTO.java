package br.com.marketflow.usuario.dto.autenticacao;

import br.com.marketflow.infra.validation.TamanhoMaximoEmBytes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @TamanhoMaximoEmBytes(72)
        String senha
) {}
