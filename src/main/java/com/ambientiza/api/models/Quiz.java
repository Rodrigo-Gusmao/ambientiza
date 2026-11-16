package com.ambientiza.api.models;

import com.ambientiza.api.enums.Temas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quiz")
    private Long idQuiz;

    @NotBlank
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tema", nullable = false)
    private Temas tema;

    @NotNull
    @Column(name = "data_disponivel", nullable = false)
    private LocalDate dataDisponivel;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuizPerguntas> perguntas = new ArrayList<>();

    public Quiz() {}

    public Quiz(String titulo, Temas tema, LocalDate dataDisponivel) {
        this.titulo = titulo;
        this.tema = tema;
        this.dataDisponivel = dataDisponivel;
    }

    public void adicionarPergunta(QuizPerguntas pergunta) {
        perguntas.add(pergunta);
        pergunta.setQuiz(this);
    }

    public void removerPergunta(QuizPerguntas pergunta) {
        perguntas.remove(pergunta);
        pergunta.setQuiz(null);
    }

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

    public Temas getTemaGeral() {
        return tema;
    }

    public void setTemaGeral(Temas temaGeral) {
        this.tema = temaGeral;
    }

    public LocalDate getDataDisponivel() {
        return dataDisponivel;
    }

    public void setDataDisponivel(LocalDate dataDisponivel) {
        this.dataDisponivel = dataDisponivel;
    }

    // Alias para compatibilidade com o typo original 'dataDispoivel'
    public LocalDate getDataDispoivel() {
        return dataDisponivel;
    }

    public void setDataDispoivel(LocalDate dataDispoivel) {
        this.dataDisponivel = dataDispoivel;
    }

    public List<QuizPerguntas> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<QuizPerguntas> perguntas) {
        this.perguntas = perguntas;
    }
}
