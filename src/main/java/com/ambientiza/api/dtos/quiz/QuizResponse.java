package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.enums.Temas;
import com.ambientiza.api.models.Quiz;

import java.time.LocalDate;
import java.util.List;

public record QuizResponse(
        Long idQuiz,
        String titulo,
        Temas tema,
        LocalDate dataDisponivel,
        List<PerguntaResponse> perguntas
) {
    public QuizResponse(Quiz quiz) {
        this(
                quiz.getIdQuiz(),
                quiz.getTitulo(),
                quiz.getTema(),
                quiz.getDataDisponivel(),
                quiz.getPerguntas() != null
                        ? quiz.getPerguntas().stream().map(PerguntaResponse::new).toList()
                        : List.of()
        );
    }
}
