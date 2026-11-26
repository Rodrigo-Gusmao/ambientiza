package com.ambientiza.api.models;

import com.ambientiza.api.enums.Temas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="quiz")
public class Quiz {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuiz;
    @NotBlank
    private String titulo;
    @NotNull @Enumerated(EnumType.STRING)
    private Temas tema;
    @NotNull
    private LocalDate dataDisponivel;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idPergunta")
    private List<QuizPerguntas> perguntas = new ArrayList<>();

    public Quiz() {}

    public Long getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(Long idQuiz) {
        this.idQuiz = idQuiz;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Temas getTema() {
        return tema;
    }

    public void setTema(Temas tema) {
        this.tema = tema;
    }

    public LocalDate getDataDisponivel() {
        return dataDisponivel;
    }

    public void setDataDisponivel(LocalDate dataDisponivel) {
        this.dataDisponivel = dataDisponivel;
    }

    public List<QuizPerguntas> getPerguntas() {
        return perguntas;
    }

    public void adicionarPergunta(QuizPerguntas pergunta) {
        pergunta.setQuiz(this);
        perguntas.add(pergunta);
    }
}
