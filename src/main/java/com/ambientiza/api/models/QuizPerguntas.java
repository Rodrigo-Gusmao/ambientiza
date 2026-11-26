package com.ambientiza.api.models;

import com.ambientiza.api.enums.NivelDificuldade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class QuizPerguntas {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPergunta;
    @ManyToOne(optional = false) @JoinColumn(name = "id_quiz", nullable = false)
    private Quiz quiz;
    @NotBlank
    private String enunciado;
    @NotNull @Enumerated(EnumType.STRING)
    private NivelDificuldade nivelDificuldade;
    @OneToMany(mappedBy = "quizPerguntas", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idQuizAlternativas")
    private List<QuizAlternativas> alternativas = new ArrayList<>();

    public QuizPerguntas() {}

    public List<QuizAlternativas> getAlternativas() {
        return alternativas;
    }

    public void adicionarAlternativa(QuizAlternativas alternativa) {
        alternativa.setQuizPerguntas(this);
        alternativas.add(alternativa);
    }

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
