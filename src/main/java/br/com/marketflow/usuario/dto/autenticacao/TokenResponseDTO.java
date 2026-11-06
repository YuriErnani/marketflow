package br.com.marketflow.usuario.dto.autenticacao;

public record TokenResponseDTO(
        String token,
        String tipo
) {}
