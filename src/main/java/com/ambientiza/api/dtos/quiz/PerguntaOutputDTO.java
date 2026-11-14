package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.enums.NivelDificuldade;

import java.util.List;

public record PerguntaOutputDTO(
        Long idPergunta,
        String enunciado,
        NivelDificuldade nivelDificuldade,
        List<AlternativaOutputDTO> alternativas
) {}