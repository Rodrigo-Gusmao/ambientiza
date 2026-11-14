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
    @NotNull
    @Enumerated(EnumType.STRING)
    private Temas tema;
    @NotNull @FutureOrPresent
    private LocalDate dataDisponivel;

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

    public void setTema(Temas temaGeral) {
        this.tema = temaGeral;
    }

    public LocalDate getDataDisponivel() {
        return dataDisponivel;
    }

    public void setDataDisponivel(LocalDate dataDispoivel) {
        this.dataDisponivel = dataDispoivel;
    }
}
