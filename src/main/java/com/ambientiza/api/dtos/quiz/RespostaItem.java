package com.ambientiza.api.dtos.quiz;

import jakarta.validation.constraints.NotNull;

public record RespostaItem(
        @NotNull(message = "O ID da pergunta é obrigatório")
        Long idPergunta,

        @NotNull(message = "O ID da alternativa selecionada é obrigatório")
        Long idAlternativa
) {
}
