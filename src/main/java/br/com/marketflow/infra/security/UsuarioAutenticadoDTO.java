package br.com.marketflow.infra.security;

import br.com.marketflow.usuario.entity.Perfil;

public record UsuarioAutenticadoDTO(
        Long id,
        Perfil perfil
) {}
