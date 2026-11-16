package com.ambientiza.api.dtos.recompensas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AtualizarRecompensa(
        @NotBlank(message = "O nome da recompensa é obrigatório")
        @Size(max = 150, message = "O nome não pode exceder 150 caracteres")
        String nome,

        @NotBlank(message = "A descrição da recompensa é obrigatória")
        @Size(max = 255, message = "A descrição não pode exceder 255 caracteres")
        String descricao,

        @NotNull(message = "O custo em pontos é obrigatório")
        @Positive(message = "O custo em pontos deve ser maior que zero")
        BigDecimal custoPontos,

        @NotNull(message = "O estoque é obrigatório")
        @PositiveOrZero(message = "O estoque não pode ser negativo")
        Integer estoque
) {
}
