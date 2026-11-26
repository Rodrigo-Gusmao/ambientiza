package com.ambientiza.api.security;

import com.ambientiza.api.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenService tokenService,
                                           UsuarioRepository usuarioRepository) throws Exception {
        return http
                // API stateless com token Bearer (sem cookies), então CSRF não se aplica
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/auth/login", "/auth/cadastro", "/error").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) ->
                                escreverErro(res, HttpServletResponse.SC_UNAUTHORIZED, "Sessão inválida ou expirada. Faça login novamente."))
                        .accessDeniedHandler((req, res, ex) ->
                                escreverErro(res, HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores.")))
                .addFilterBefore(new TokenAuthFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private static void escreverErro(HttpServletResponse res, int status, String mensagem) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"message\":\"" + mensagem + "\"}");
    }
}
