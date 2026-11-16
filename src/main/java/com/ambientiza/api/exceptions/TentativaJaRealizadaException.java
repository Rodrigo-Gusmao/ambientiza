package com.ambientiza.api.exceptions;

public class TentativaJaRealizadaException extends RegraDeNegocioException {

    public TentativaJaRealizadaException(String mensagem) {
        super(mensagem);
    }

    public TentativaJaRealizadaException() {
        super("Você já realizou a tentativa diária deste quiz hoje. Limite: 1 tentativa por dia.");
    }
}
