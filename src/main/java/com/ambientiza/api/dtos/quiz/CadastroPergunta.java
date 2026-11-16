package com.ambientiza.api.dtos.quiz;

import com.ambientiza.api.enums.NivelDificuldade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CadastroPergunta(
        @NotBlank(message = "O enunciado da pergunta é obrigatório")
        @Size(max = 255, message = "O enunciado deve ter no máximo 255 caracteres")
        String enunciado,

        @NotNull(message = "O nível de dificuldade é obrigatório")
        NivelDificuldade nivelDificuldade,

        @NotEmpty(message = "A pergunta deve conter ao menos duas alternativas")
        @Size(min = 2, max = 5, message = "A pergunta deve ter entre 2 e 5 alternativas")
        List<@Valid CadastroAlternativa> alternativas
) {
}
