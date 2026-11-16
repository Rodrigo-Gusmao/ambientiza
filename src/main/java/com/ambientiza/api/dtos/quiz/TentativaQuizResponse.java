package com.ambientiza.api.dtos.quiz;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TentativaQuizResponse(
        Long idTentativa,
        Long idQuiz,
        String tituloQuiz,
        Long idUsuario,
        String nomeUsuario,
        LocalDate dataJogada,
        boolean acertoGeral,
        int totalPerguntas,
        int totalAcertos,
        int pontosGanhos,
        BigDecimal saldoAtualizado,
        Integer ofensivaAtualizada,
        List<ResultadoPergunta> detalhesRespostas,
        String mensagemFeedback
) {
}
