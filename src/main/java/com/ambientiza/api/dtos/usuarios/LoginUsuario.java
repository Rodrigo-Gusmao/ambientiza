package com.ambientiza.api.dtos.usuarios;

import jakarta.validation.constraints.NotBlank;

public record LoginUsuario(
        @NotBlank String nome,
        @NotBlank String senha
) {
}