package com.ambientiza.api.models;

import com.ambientiza.api.enums.Temas;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name="quiz")
public class Quiz {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuiz;
    @NotBlank
    private String titulo;
    @NotBlank
    private Temas tema;
    @NotNull @FutureOrPresent
    private LocalDate dataDispoivel;

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

    public Temas getTemaGeral() {
        return tema;
    }

    public void setTemaGeral(Temas temaGeral) {
        this.tema = temaGeral;
    }

    public LocalDate getDataDispoivel() {
        return dataDispoivel;
    }

    public void setDataDispoivel(LocalDate dataDispoivel) {
        this.dataDispoivel = dataDispoivel;
    }
}
