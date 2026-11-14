package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.enums.NivelDificuldade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PerguntaInputDTO(
        @NotBlank String enunciado,
        @NotNull NivelDificuldade nivelDificuldade,
        @NotEmpty List<AlternativaInputDTO> alternativas
) {}