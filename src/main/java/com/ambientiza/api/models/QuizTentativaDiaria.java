package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Entity
public class QuizTentativaDiaria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuizTentativaDiaria;
    @ManyToOne @JoinColumn(name = "id_quiz", nullable = false)
    private Quiz quiz;
    @OneToOne @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;
    @NotNull @PastOrPresent
    private LocalDate dataJogada;
    @NotNull @PositiveOrZero
    private Integer acerto;
    @NotNull @PositiveOrZero
    private Integer pontosGanhos;

    public QuizTentativaDiaria() {}

    public Long getIdQuizTentativaDiaria() {
        return idQuizTentativaDiaria;
    }

    public void setIdQuizTentativaDiaria(Long idQuizTentativaDiaria) {
        this.idQuizTentativaDiaria = idQuizTentativaDiaria;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataJogada() {
        return dataJogada;
    }

    public void setDataJogada(LocalDate dataJogada) {
        this.dataJogada = dataJogada;
    }

    public Integer getAcerto() {
        return acerto;
    }

    public void setAcerto(Integer acerto) {
        this.acerto = acerto;
    }

    public Integer getPontosGanhos() {
        return pontosGanhos;
    }

    public void setPontosGanhos(Integer pontosGanhos) {
        this.pontosGanhos = pontosGanhos;
    }
}
