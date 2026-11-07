package br.com.marketflow.usuario.dto;

import br.com.marketflow.usuario.entity.Perfil;

public record UsuarioAuthRequestDTO(
        Long id,
        Perfil perfil
) {

    public boolean ehAdmin() {
        return perfil == Perfil.ADMIN;
    }

}
