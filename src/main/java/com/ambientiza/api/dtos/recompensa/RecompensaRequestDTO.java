package com.ambientiza.api.dtos.recompensa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RecompensaRequestDTO(
        @NotBlank String nome,
        @NotBlank String descricao,
        @NotNull @Positive BigDecimal custo,
        @NotNull @PositiveOrZero BigDecimal estoque
) {}