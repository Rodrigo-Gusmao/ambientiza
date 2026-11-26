package com.ambientiza.api.enums;

public enum NivelDificuldade {
    FACIL(10),
    MEDIO(30),
    DIFICIL(50);

    private final int pontos;

    NivelDificuldade(int pontos) {
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }
}
