package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Entity
@Table(name = "quiz_tentativa_diaria", uniqueConstraints = {
        @UniqueConstraint(name = "uq_usuario_quiz_dia", columnNames = {"id_usuario", "id_quiz", "data_jogada"})
})
public class QuizTentativaDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tentativa")
    private Long idTentativa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quiz", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;

    @NotNull
    @PastOrPresent
    @Column(name = "data_jogada", nullable = false)
    private LocalDate dataJogada;

    @NotNull
    @Column(name = "acerto", nullable = false)
    private Boolean acerto;

    @NotNull
    @PositiveOrZero
    @Column(name = "pontos_ganhos", nullable = false)
    private Integer pontosGanhos;

    public QuizTentativaDiaria() {
        this.dataJogada = LocalDate.now();
    }

    public QuizTentativaDiaria(Quiz quiz, Usuarios usuario, Boolean acerto, Integer pontosGanhos) {
        this.quiz = quiz;
        this.usuario = usuario;
        this.dataJogada = LocalDate.now();
        this.acerto = acerto;
        this.pontosGanhos = pontosGanhos != null ? pontosGanhos : 0;
    }

    public Long getIdTentativa() {
        return idTentativa;
    }

    public void setIdTentativa(Long idTentativa) {
        this.idTentativa = idTentativa;
    }

    // Aliases para compatibilidade retroativa
    public Long getIdQuizTentativaDiaria() {
        return idTentativa;
    }

    public void setIdQuizTentativaDiaria(Long idQuizTentativaDiaria) {
        this.idTentativa = idQuizTentativaDiaria;
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

    public Boolean getAcerto() {
        return acerto;
    }

    public void setAcerto(Boolean acerto) {
        this.acerto = acerto;
    }

    public Integer getPontosGanhos() {
        return pontosGanhos;
    }

    public void setPontosGanhos(Integer pontosGanhos) {
        this.pontosGanhos = pontosGanhos;
    }
}
