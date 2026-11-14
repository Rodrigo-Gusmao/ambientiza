package com.ambientiza.api.dtos.recompensa;

import jakarta.validation.constraints.NotNull;

public record ResgateRecompensaRequestDTO(
        @NotNull Long idUsuario,
        @NotNull Long idRecompensa
) {}