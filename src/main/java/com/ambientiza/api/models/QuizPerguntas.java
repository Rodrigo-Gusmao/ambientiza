package com.ambientiza.api.models;

import com.ambientiza.api.enums.NivelDificuldade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_perguntas")
public class QuizPerguntas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pergunta")
    private Long idPergunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quiz", nullable = false)
    private Quiz quiz;

    @NotBlank
    @Column(name = "enunciado", length = 255, nullable = false)
    private String enunciado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_dificuldade", nullable = false)
    private NivelDificuldade nivelDificuldade;

    @OneToMany(mappedBy = "quizPerguntas", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuizAlternativas> alternativas = new ArrayList<>();

    public QuizPerguntas() {}

    public QuizPerguntas(Quiz quiz, String enunciado, NivelDificuldade nivelDificuldade) {
        this.quiz = quiz;
        this.enunciado = enunciado;
        this.nivelDificuldade = nivelDificuldade;
    }

    public void adicionarAlternativa(QuizAlternativas alternativa) {
        alternativas.add(alternativa);
        alternativa.setQuizPerguntas(this);
    }

    public void removerAlternativa(QuizAlternativas alternativa) {
        alternativas.remove(alternativa);
        alternativa.setQuizPerguntas(null);
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

    public List<QuizAlternativas> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<QuizAlternativas> alternativas) {
        this.alternativas = alternativas;
    }
}
