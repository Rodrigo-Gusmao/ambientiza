package com.ambientiza.api.dtos.usuarios;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AtualizarPontos(
        @NotNull(message = "A quantidade de pontos é obrigatória")
        @Positive(message = "A quantidade de pontos deve ser estritamente positiva")
        BigDecimal pontos
) {
}
