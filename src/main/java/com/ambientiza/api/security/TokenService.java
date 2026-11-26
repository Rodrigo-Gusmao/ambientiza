package com.ambientiza.api.security;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Tokens opacos em memória: simples e suficiente enquanto o banco também é em memória (H2).
@Service
public class TokenService {
    private static final Duration VALIDADE = Duration.ofHours(8);

    private record Sessao(Long idUsuario, Instant expiraEm) {}

    private final SecureRandom random = new SecureRandom();
    private final Map<String, Sessao> sessoes = new ConcurrentHashMap<>();

    public String gerar(Long idUsuario) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sessoes.put(token, new Sessao(idUsuario, Instant.now().plus(VALIDADE)));
        return token;
    }

    public Optional<Long> validar(String token) {
        Sessao sessao = sessoes.get(token);
        if (sessao == null) return Optional.empty();
        if (sessao.expiraEm().isBefore(Instant.now())) {
            sessoes.remove(token);
            return Optional.empty();
        }
        return Optional.of(sessao.idUsuario());
    }

    public void revogar(String token) {
        sessoes.remove(token);
    }

    public void revogarDoUsuario(Long idUsuario) {
        sessoes.values().removeIf(s -> s.idUsuario().equals(idUsuario));
    }
}
