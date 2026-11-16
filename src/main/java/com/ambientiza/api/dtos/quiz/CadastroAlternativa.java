package com.ambientiza.api.dtos.quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroAlternativa(
        @NotBlank(message = "O texto da alternativa é obrigatório")
        @Size(max = 255, message = "O texto da alternativa deve ter no máximo 255 caracteres")
        String texto,

        @NotNull(message = "O indicador se a alternativa é correta é obrigatório")
        Boolean correta
) {
}
