package com.ambientiza.api.dtos.usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUsuario(
        @NotBlank @Email String email,
        @NotBlank String senha
) {
}