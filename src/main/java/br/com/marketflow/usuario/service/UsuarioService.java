package br.com.marketflow.usuario.service;

import br.com.marketflow.usuario.dto.AlterarSenhaRequestDTO;
import br.com.marketflow.usuario.dto.AtualizarContaRequestDTO;
import br.com.marketflow.usuario.dto.UsuarioResponseDTO;
import br.com.marketflow.usuario.entity.Perfil;
import br.com.marketflow.usuario.entity.Usuario;
import br.com.marketflow.usuario.exception.AdminNaoPodeSerDesativadoException;
import br.com.marketflow.usuario.exception.CredenciaisInvalidasException;
import br.com.marketflow.usuario.exception.EmailJaCadastradoException;
import br.com.marketflow.usuario.exception.SenhaAtualInvalidaException;
import br.com.marketflow.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarUsuario(Long id) {
        return usuarioRepository
                .findById(id)
                .map(UsuarioResponseDTO::from)
                .orElseThrow(CredenciaisInvalidasException::new);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, AtualizarContaRequestDTO request) {
        var usuario = buscarUsuarioPorId(id);

        var email = request.email().trim().toLowerCase(Locale.ROOT);

        if (!email.equals(usuario.getEmail()) && usuarioRepository.existsByEmail(email))
            throw new EmailJaCadastradoException();

        usuario.atualizarDados(request.nome().trim(), email);

        return UsuarioResponseDTO
                .from(usuario);
    }

    @Transactional
    public void alterarSenha(Long id, AlterarSenhaRequestDTO request) {
        var usuario = buscarUsuarioPorId(id);

        if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenhaHash()))
            throw new SenhaAtualInvalidaException();

        usuario.alterarSenha(passwordEncoder.encode(request.novaSenha()));
    }

    @Transactional
    public void desativarUsuario(Long id) {
        var usuario = buscarUsuarioPorId(id);

        if (usuario.getPerfil() == Perfil.ADMIN)
            throw new AdminNaoPodeSerDesativadoException();

        usuario.desativar();
    }

    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository
                .findById(id)
                .orElseThrow(CredenciaisInvalidasException::new);
    }

}
