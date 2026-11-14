package com.ambientiza.api.models;

import com.ambientiza.api.enums.NivelDificuldade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class QuizPerguntas {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPergunta;
    @ManyToOne @JoinColumn(name = "id_quiz")
    private Quiz quiz;
    @NotBlank
    private String enunciado;
    @NotNull
    @Enumerated(EnumType.STRING)
    private NivelDificuldade nivelDificuldade;

    public QuizPerguntas() {}

    public Long getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(Long idPergunta) {
        this.idPergunta = idPergunta;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public NivelDificuldade getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(NivelDificuldade nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }

}
