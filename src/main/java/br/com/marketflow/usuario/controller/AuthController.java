package br.com.marketflow.usuario.controller;

import br.com.marketflow.usuario.dto.CadastroRequestDTO;
import br.com.marketflow.usuario.dto.LoginRequestDTO;
import br.com.marketflow.usuario.dto.TokenResponseDTO;
import br.com.marketflow.usuario.dto.UsuarioResponseDTO;
import br.com.marketflow.usuario.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO cadastrar(@Valid @RequestBody CadastroRequestDTO request) {
        return authService.cadastrar(request);
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

}
