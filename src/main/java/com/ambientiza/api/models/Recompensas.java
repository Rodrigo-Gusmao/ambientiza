package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


import java.math.BigDecimal;

@Entity
@Table(name="recompensas")
public class Recompensas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecompensa;
    @NotBlank
    private String nome;
    @NotBlank
    private String descricao;
    @NotNull @PositiveOrZero
    private BigDecimal custo;
    @NotNull @PositiveOrZero
    private BigDecimal estoque;

    public Recompensas() {}

    public Long getIdRecompensa() {
        return idRecompensa;
    }

    public void setIdRecompensa(Long idRecompensa) {
        this.idRecompensa = idRecompensa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public BigDecimal getEstoque() {
        return estoque;
    }

    public void setEstoque(BigDecimal estoque) {
        this.estoque = estoque;
    }
}
