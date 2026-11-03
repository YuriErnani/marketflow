package br.com.marketflow.usuario.service;

import br.com.marketflow.infra.security.TokenService;
import br.com.marketflow.usuario.dto.CadastroRequestDTO;
import br.com.marketflow.usuario.dto.LoginRequestDTO;
import br.com.marketflow.usuario.dto.TokenResponseDTO;
import br.com.marketflow.usuario.dto.UsuarioResponseDTO;
import br.com.marketflow.usuario.entity.Perfil;
import br.com.marketflow.usuario.entity.Usuario;
import br.com.marketflow.usuario.exception.CredenciaisInvalidasException;
import br.com.marketflow.usuario.exception.EmailJaCadastradoException;
import br.com.marketflow.usuario.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public UsuarioResponseDTO cadastrar(CadastroRequestDTO request) {
        var email = request.email().trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(email))
            throw new EmailJaCadastradoException();

        var senhaHash = passwordEncoder.encode(request.senha());

        var usuario = new Usuario(request.nome().trim(), email, senhaHash, Perfil.CLIENTE);

        return UsuarioResponseDTO
                .from(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public TokenResponseDTO login(LoginRequestDTO request) {
        var email = request.email().trim().toLowerCase(Locale.ROOT);

        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.isAtivo() || !passwordEncoder.matches(request.senha(), usuario.getSenhaHash()))
            throw new CredenciaisInvalidasException();

        return new TokenResponseDTO(tokenService.gerar(usuario), "Bearer");
    }

}
