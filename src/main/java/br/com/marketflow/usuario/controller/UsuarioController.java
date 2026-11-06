package br.com.marketflow.usuario.controller;

import br.com.marketflow.usuario.dto.UsuarioAuthRequestDTO;
import br.com.marketflow.usuario.dto.AlterarSenhaRequestDTO;
import br.com.marketflow.usuario.dto.AtualizarContaRequestDTO;
import br.com.marketflow.usuario.dto.UsuarioResponseDTO;
import br.com.marketflow.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios/minha-conta")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public UsuarioResponseDTO buscarUsuario(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario) {
        return usuarioService.buscarUsuario(usuario.id());
    }

    @PutMapping
    public UsuarioResponseDTO atualizarUsuario(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @Valid @RequestBody AtualizarContaRequestDTO request) {
        return usuarioService.atualizarUsuario(usuario.id(), request);
    }

    @PutMapping("/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @Valid @RequestBody AlterarSenhaRequestDTO request) {
        usuarioService.alterarSenha(usuario.id(), request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarUsuario(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario) {
        usuarioService.desativarUsuario(usuario.id());
    }

}
