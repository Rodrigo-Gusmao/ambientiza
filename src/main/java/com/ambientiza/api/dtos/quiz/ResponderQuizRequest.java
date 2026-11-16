package com.ambientiza.api.dtos.quiz;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ResponderQuizRequest(
        @NotNull(message = "O ID do usuário é obrigatório")
        Long idUsuario,

        @NotNull(message = "O ID do quiz é obrigatório")
        Long idQuiz,

        @NotEmpty(message = "A lista de respostas não pode estar vazia")
        List<@Valid RespostaItem> respostas
) {
}
