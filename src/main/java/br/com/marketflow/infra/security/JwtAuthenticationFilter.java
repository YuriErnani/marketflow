package br.com.marketflow.infra.security;

import br.com.marketflow.usuario.entity.Usuario;
import br.com.marketflow.usuario.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIXO = "Bearer ";

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var cabecalho = request.getHeader("Authorization");

        if (cabecalho != null && cabecalho.startsWith(PREFIXO)) {
            var token = cabecalho.substring(PREFIXO.length());

            tokenService
                    .validarToken(token)
                    .flatMap(usuarioRepository::findById)
                    .filter(Usuario::isAtivo)
                    .ifPresent(this::autenticar);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticar(Usuario usuario) {
        var principal = new UsuarioAutenticadoDTO(usuario.getId(), usuario.getPerfil());

        var permissoes = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()));

        var autenticacao = new UsernamePasswordAuthenticationToken(principal, null, permissoes);

        SecurityContextHolder
                .getContext()
                .setAuthentication(autenticacao);
    }

}
