package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "quiz_alternativas")
public class QuizAlternativas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alternativa")
    private Long idAlternativa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pergunta", nullable = false)
    private QuizPerguntas quizPerguntas;

    @NotBlank
    @Column(name = "texto", length = 255, nullable = false)
    private String texto;

    @NotNull
    @Column(name = "correta", nullable = false)
    private Boolean correta;

    public QuizAlternativas() {}

    public QuizAlternativas(QuizPerguntas quizPerguntas, String texto, Boolean correta) {
        this.quizPerguntas = quizPerguntas;
        this.texto = texto;
        this.correta = correta;
    }

    public Long getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(Long idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    // Aliases para compatibilidade retroativa com 'idQuizAlternativas'
    public Long getIdQuizAlternativas() {
        return idAlternativa;
    }

    public void setIdQuizAlternativas(Long idQuizAlternativas) {
        this.idAlternativa = idQuizAlternativas;
    }

    public QuizPerguntas getQuizPerguntas() {
        return quizPerguntas;
    }

    public void setQuizPerguntas(QuizPerguntas quizPerguntas) {
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
