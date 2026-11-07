package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class QuizAlternativasModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuizAlternativas;
    @ManyToOne @JoinColumn(name = "id_perguntas")
    private QuizPerguntasModel quizPerguntas;
    @NotBlank
    private String texto;
    @NotNull
    private Boolean correta;

    public QuizAlternativasModel() {}

    public Long getIdQuizAlternativas() {
        return idQuizAlternativas;
    }

    public void setIdQuizAlternativas(Long idQuizAlternativas) {
        this.idQuizAlternativas = idQuizAlternativas;
    }

    public QuizPerguntasModel getQuizPerguntas() {
        return quizPerguntas;
    }

    public void setQuizPerguntas(QuizPerguntasModel quizPerguntas) {
        this.quizPerguntas = quizPerguntas;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Boolean getCorreta() {
        return correta;
    }

    public void setCorreta(Boolean correta) {
        this.correta = correta;
    }
}
