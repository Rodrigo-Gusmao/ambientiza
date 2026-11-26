package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

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
    private Integer custo;
    @NotNull @PositiveOrZero
    private Integer estoque;

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

    public Integer getCusto() {
        return custo;
    }

    public void setCusto(Integer custo) {
        this.custo = custo;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}
