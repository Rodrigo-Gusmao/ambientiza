package com.ambientiza.api.dtos.usuarios;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public record LoginUsuario(
        @NotBlank(message = "O identificador (nome ou email) é obrigatório")
        @JsonAlias({"nome", "email", "usuario"})
        String login,

        @NotBlank(message = "A senha é obrigatória")
        String senha
) {
    public String nome() {
        return login;
    }
}
