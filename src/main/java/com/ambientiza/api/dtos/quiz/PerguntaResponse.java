package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.enums.NivelDificuldade;
import com.ambientiza.api.models.QuizPerguntas;

import java.util.List;

public record PerguntaResponse(
        Long idPergunta,
        String enunciado,
        NivelDificuldade nivelDificuldade,
        List<AlternativaResponse> alternativas
) {
    public PerguntaResponse(QuizPerguntas pergunta) {
        this(
                pergunta.getIdPergunta(),
                pergunta.getEnunciado(),
                pergunta.getNivelDificuldade(),
                pergunta.getAlternativas() != null
                        ? pergunta.getAlternativas().stream().map(AlternativaResponse::new).toList()
                        : List.of()
        );
    }
}
