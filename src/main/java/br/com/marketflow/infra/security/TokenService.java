package br.com.marketflow.infra.security;

import br.com.marketflow.usuario.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {

    private static final String EMISSOR = "marketflow";
    private static final Duration VALIDADE = Duration.ofHours(2);

    private final Algorithm algoritmo;

    public TokenService(@Value("${api.security.jwt.secret}") String segredo) {
        this.algoritmo = Algorithm.HMAC256(segredo);
    }

    public String gerarToken(Usuario usuario) {
        var agora = Instant.now();

        return JWT
                .create()
                .withIssuer(EMISSOR)
                .withSubject(usuario.getId().toString())
                .withClaim("perfil", usuario.getPerfil().name())
                .withIssuedAt(agora)
                .withExpiresAt(agora.plus(VALIDADE))
                .sign(algoritmo);
    }

    public Optional<Long> validarToken(String token) {
        try {
            var subject = JWT
                    .require(algoritmo)
                    .withIssuer(EMISSOR)
                    .build()
                    .verify(token)
                    .getSubject();

            return Optional.of(Long.valueOf(subject));
        } catch (JWTVerificationException | NumberFormatException e) {
            return Optional.empty();
        }
    }

}
