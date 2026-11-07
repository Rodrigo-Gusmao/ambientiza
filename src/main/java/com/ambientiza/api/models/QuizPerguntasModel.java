package com.ambientiza.api.models;

import com.ambientiza.api.enums.NivelDificuldade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class QuizPerguntasModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPergunta;
    @ManyToOne @JoinColumn(name = "id_quiz")
    private QuizModel quiz;
    @NotBlank
    private String enunciado;
    @NotBlank
    // rever tema - criar tebela propria
    private String tema;
    // ENUM('facil', 'medio', 'dificil')
    private NivelDificuldade nivelDificuldade;

    public QuizPerguntasModel() {}

    public Long getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(Long idPergunta) {
        this.idPergunta = idPergunta;
    }

    public QuizModel getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizModel quiz) {
        this.quiz = quiz;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public NivelDificuldade getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(NivelDificuldade nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }
}
