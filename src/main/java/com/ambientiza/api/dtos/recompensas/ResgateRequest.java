package com.ambientiza.api.dtos.recompensas;

import jakarta.validation.constraints.NotNull;

public record ResgateRequest(
        @NotNull(message = "O ID do usuário é obrigatório")
        Long idUsuario,

        @NotNull(message = "O ID da recompensa é obrigatório")
        Long idRecompensa
) {
}
