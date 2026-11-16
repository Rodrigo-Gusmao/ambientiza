package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.models.QuizAlternativas;

public record AlternativaAdminResponse(
        Long idAlternativa,
        String texto,
        Boolean correta
) {
    public AlternativaAdminResponse(QuizAlternativas alternativa) {
        this(
                alternativa.getIdAlternativa(),
                alternativa.getTexto(),
                alternativa.getCorreta()
        );
    }
}
