package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.enums.Temas;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CadastroQuiz(
        @NotBlank(message = "O título do quiz é obrigatório")
        @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
        String titulo,

        @NotNull(message = "O tema do quiz é obrigatório")
        Temas tema,

        @NotNull(message = "A data de disponibilidade é obrigatória")
        LocalDate dataDisponivel,

        @NotEmpty(message = "O quiz deve conter ao menos uma pergunta")
        List<@Valid CadastroPergunta> perguntas
) {
}
