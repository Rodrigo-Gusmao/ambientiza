package com.ambientiza.api.models;

import com.ambientiza.api.enums.Tipo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Tarefas {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarefa;
    @NotBlank
    private String titulo;
    @NotBlank
    private String descricao;
    @NotNull @PositiveOrZero
    private Integer pontosRecompensa;
    @NotNull @Enumerated(EnumType.STRING)
    private Tipo tipo;

    public Tarefas() {}

    public Long getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(Long idTarefa) {
        this.idTarefa = idTarefa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getPontosRecompensa() {
        return pontosRecompensa;
    }

    public void setPontosRecompensa(Integer pontosRecompensa) {
        this.pontosRecompensa = pontosRecompensa;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}
