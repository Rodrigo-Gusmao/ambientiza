package com.ambientiza.api.dtos.recompensas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AtualizarEstoque(
        @NotNull(message = "A quantidade a adicionar no estoque é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {
}
