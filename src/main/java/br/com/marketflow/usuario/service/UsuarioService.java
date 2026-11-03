package br.com.marketflow.usuario.service;

import br.com.marketflow.usuario.dto.UsuarioResponseDTO;
import br.com.marketflow.usuario.exception.CredenciaisInvalidasException;
import br.com.marketflow.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        return usuarioRepository
                .findById(id)
                .map(UsuarioResponseDTO::from)
                .orElseThrow(CredenciaisInvalidasException::new);
    }

}
