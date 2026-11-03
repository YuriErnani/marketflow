package br.com.marketflow.usuario.controller;

import br.com.marketflow.infra.security.UsuarioAutenticadoDTO;
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
    public UsuarioResponseDTO buscarDados(@AuthenticationPrincipal UsuarioAutenticadoDTO usuario) {
        return usuarioService.buscarPorId(usuario.id());
    }

    @PutMapping
    public UsuarioResponseDTO atualizar(@AuthenticationPrincipal UsuarioAutenticadoDTO usuario, @Valid @RequestBody AtualizarContaRequestDTO request) {
        return usuarioService.atualizar(usuario.id(), request);
    }

    @PutMapping("/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@AuthenticationPrincipal UsuarioAutenticadoDTO usuario, @Valid @RequestBody AlterarSenhaRequestDTO request) {
        usuarioService.alterarSenha(usuario.id(), request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@AuthenticationPrincipal UsuarioAutenticadoDTO usuario) {
        usuarioService.desativar(usuario.id());
    }

}
