package br.com.marketflow.infra.configuration;

import br.com.marketflow.usuario.entity.Perfil;
import br.com.marketflow.usuario.entity.Usuario;
import br.com.marketflow.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AdminInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String senha;

    public AdminInitializer(@Value("${api.admin.email}") String email, @Value("${api.admin.senha}") String senha, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.email = email;
        this.senha = senha;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (email.isBlank() || senha.isBlank())
            return;

        var emailNormalizado = email.trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(emailNormalizado))
            return;

        var admin = new Usuario("Administrador", emailNormalizado, passwordEncoder.encode(senha), Perfil.ADMIN);

        usuarioRepository.save(admin);
    }

}
