package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.models.QuizAlternativas;

public record AlternativaResponse(
        Long idAlternativa,
        String texto
) {
    public AlternativaResponse(QuizAlternativas alternativa) {
        this(
                alternativa.getIdAlternativa(),
                alternativa.getTexto()
        );
    }
}
