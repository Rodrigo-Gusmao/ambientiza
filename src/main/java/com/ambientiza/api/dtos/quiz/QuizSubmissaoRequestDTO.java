package com.ambientiza.api.dtos.quiz;

import jakarta.validation.constraints.NotNull;

public record QuizSubmissaoRequestDTO(
        @NotNull Long idUsuario,
        @NotNull Long idQuiz,
        @NotNull Long idAlternativaEscolhida
) {}