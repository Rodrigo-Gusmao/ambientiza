package com.ambientiza.api.dtos.quiz;

import java.math.BigDecimal;
import java.time.LocalDate;

public record QuizTentativaResponseDTO(
        Long idTentativa,
        LocalDate dataJogada,
        Boolean acerto,
        Integer pontosGanhos,
        BigDecimal saldoAtualizado,
        Integer ofensivaAtualizada
) {}