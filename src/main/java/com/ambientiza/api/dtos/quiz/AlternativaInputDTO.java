package com.ambientiza.api.dtos.quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlternativaInputDTO(
        @NotBlank String texto,
        @NotNull Boolean correta
) {}
