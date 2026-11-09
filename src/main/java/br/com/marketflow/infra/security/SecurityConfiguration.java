package br.com.marketflow.infra.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final HandlerExceptionResolver resolvedorDeErros;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolvedorDeErros) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.resolvedorDeErros = resolvedorDeErros;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth/cadastro",
                                "/api/v1/auth/login")
                        .permitAll()

                        .requestMatchers(
                                "/actuator/health/**",
                                "/error",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/categorias/**",
                                "/api/v1/produtos/**")
                        .permitAll()

                        .requestMatchers(
                                "/api/v1/categorias/**",
                                "/api/v1/produtos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/v1/estoque/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/v1/carrinho/**")
                        .hasRole("CLIENTE")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/pedidos")
                        .hasRole("CLIENTE")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/pedidos/*/status")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/pedidos/*/pagamentos")
                        .hasRole("CLIENTE")

                        .anyRequest().authenticated())

                .exceptionHandling(erros -> erros
                        .authenticationEntryPoint((request, response, excecao) ->
                                resolvedorDeErros.resolveException(request, response, null, excecao))

                        .accessDeniedHandler((request, response, excecao) ->
                                resolvedorDeErros.resolveException(request, response, null, excecao)))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
