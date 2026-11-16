package com.ambientiza.api.dtos.usuarios;

import com.ambientiza.api.models.Usuarios;

import java.time.LocalDate;

public record OfensivaResponse(
        Long idUsuario,
        String nome,
        Integer ofensivaAtual,
        LocalDate ultimaAtividade,
        boolean atividadeRealizadaHoje
) {
    public OfensivaResponse(Usuarios usuario) {
        this(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getOfensivaAtual(),
                usuario.getUltimaAtividade(),
                usuario.getUltimaAtividade() != null && usuario.getUltimaAtividade().equals(LocalDate.now())
        );
    }
}
