package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.enums.Temas;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record QuizRequestDTO(
        @NotBlank String titulo,
        @NotNull Temas tema,
        @NotNull @FutureOrPresent LocalDate dataDisponivel,
        @NotEmpty List<PerguntaInputDTO> perguntas
){}