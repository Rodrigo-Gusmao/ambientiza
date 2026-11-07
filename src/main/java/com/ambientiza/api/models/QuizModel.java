package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

@Entity
@Table(name="quiz")
public class QuizModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuiz;
    @NotBlank
    private String titulo;
    @NotBlank
    private String temaGeral;
    @NotNull @FutureOrPresent
    private LocalDate dataDispoivel;

    public QuizModel() {}

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

    public String getTemaGeral() {
        return temaGeral;
    }

    public void setTemaGeral(String temaGeral) {
        this.temaGeral = temaGeral;
    }

    public LocalDate getDataDispoivel() {
        return dataDispoivel;
    }

    public void setDataDispoivel(LocalDate dataDispoivel) {
        this.dataDispoivel = dataDispoivel;
    }
}
