package com.ambientiza.api.exceptions;

public class EstoqueInsuficienteException extends RegraDeNegocioException {

    public EstoqueInsuficienteException(String nomeRecompensa) {
        super(String.format("A recompensa '%s' não possui estoque disponível para resgate.", nomeRecompensa));
    }
}
