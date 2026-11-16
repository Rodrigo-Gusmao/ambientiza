package com.ambientiza.api.exceptions;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RegraDeNegocioException {

    public SaldoInsuficienteException(BigDecimal saldoAtual, BigDecimal valorTentado) {
        super(String.format("Saldo insuficiente para realizar a operação. Saldo atual: %s pontos, valor necessário: %s pontos.",
                saldoAtual != null ? saldoAtual.toPlainString() : "0.00",
                valorTentado != null ? valorTentado.toPlainString() : "0.00"));
    }

    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
