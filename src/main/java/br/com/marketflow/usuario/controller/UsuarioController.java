package br.com.marketflow.usuario.controller;

import br.com.marketflow.infra.security.UsuarioAutenticadoDTO;
import br.com.marketflow.usuario.dto.UsuarioResponseDTO;
import br.com.marketflow.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/minha-conta")
    public UsuarioResponseDTO buscarDados(@AuthenticationPrincipal UsuarioAutenticadoDTO usuario) {
        return usuarioService
                .buscarPorId(usuario.id());
    }

}
