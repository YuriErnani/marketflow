package br.com.marketflow.usuario.controller;

import br.com.marketflow.usuario.dto.autenticacao.CadastroRequestDTO;
import br.com.marketflow.usuario.dto.autenticacao.LoginRequestDTO;
import br.com.marketflow.usuario.dto.autenticacao.TokenResponseDTO;
import br.com.marketflow.usuario.dto.UsuarioResponseDTO;
import br.com.marketflow.usuario.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação", description = "Cadastro de clientes e login")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO cadastrarUsuario(@Valid @RequestBody CadastroRequestDTO request) {
        return authService.cadastrarUsuario(request);
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

}
