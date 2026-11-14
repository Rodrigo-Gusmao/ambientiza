package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.enums.Temas;

import java.time.LocalDate;
import java.util.List;

public record QuizExibicaoResponseDTO(
        Long idQuiz,
        String titulo,
        Temas tema,
        LocalDate dataDisponivel,
        List<PerguntaOutputDTO> perguntas
) {}
