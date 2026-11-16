package com.ambientiza.api.dtos.quiz;

public record ResultadoPergunta(
        Long idPergunta,
        String enunciado,
        Long idAlternativaEscolhida,
        Long idAlternativaCorreta,
        boolean acertou,
        int pontosGanhos
) {
}
