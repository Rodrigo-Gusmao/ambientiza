package com.ambientiza.api.dtos.usuarios;

import jakarta.validation.constraints.NotBlank;

public record Cadastro(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String senha
) {
}
