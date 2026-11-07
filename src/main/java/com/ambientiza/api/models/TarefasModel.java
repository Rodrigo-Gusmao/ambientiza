package com.ambientiza.api.models;

import com.ambientiza.api.enums.Tipo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class TarefasModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarefa;
    @NotBlank
    private String titulo;
    @NotBlank
    private String descricao;
    @NotNull
    private BigDecimal pontosRecompensa;
    @NotBlank
    // ENUM('diário', 'semanal')
    private Tipo tipo;

    public TarefasModel() {}

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

    public BigDecimal getPontosRecompensa() {
        return pontosRecompensa;
    }

    public void setPontosRecompensa(BigDecimal pontosRecompensa) {
        this.pontosRecompensa = pontosRecompensa;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}
