package com.ambientiza.api.dtos.usuarios;

import jakarta.validation.constraints.NotBlank;

public record ListarNomes(
        @NotBlank String nome
) {
}
