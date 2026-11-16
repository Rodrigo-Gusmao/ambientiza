package com.ambientiza.api.dtos.usuarios;

import com.ambientiza.api.models.Usuarios;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UsuarioResponse(
        Long idUsuario,
        String nome,
        String email,
        BigDecimal saldo,
        Integer ofensivaAtual,
        LocalDate ultimaAtividade,
        LocalDate criadoEm
) {
    public UsuarioResponse(Usuarios usuario) {
        this(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSaldo(),
                usuario.getOfensivaAtual(),
                usuario.getUltimaAtividade(),
                usuario.getCriadoEm()
        );
    }
}
