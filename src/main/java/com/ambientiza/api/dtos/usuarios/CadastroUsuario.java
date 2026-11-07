package com.ambientiza.api.dtos.usuarios;

import jakarta.validation.constraints.NotBlank;

public record CadastroUsuario(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String senha
) {
}
