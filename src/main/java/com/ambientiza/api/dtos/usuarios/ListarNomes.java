package com.ambientiza.api.dtos.usuarios;

import com.ambientiza.api.models.Usuarios;
import jakarta.validation.constraints.NotBlank;

public record ListarNomes(
        Long idUsuario,
        @NotBlank String nome
) {
    public ListarNomes(String nome) {
        this(null, nome);
    }

    public ListarNomes(Usuarios usuario) {
        this(usuario.getIdUsuario(), usuario.getNome());
    }
}
