package com.ambientiza.api.dtos.quiz;

import java.time.LocalDate;

public record StatusQuizDiarioResponse(
        Long idQuiz,
        String tituloQuiz,
        LocalDate dataDisponivel,
        boolean jaRespondidoHoje,
        Integer pontosGanhos,
        Boolean acerto
) {
}
