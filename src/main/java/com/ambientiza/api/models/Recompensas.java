package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
@Table(name = "recompensas")
public class Recompensas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recompensa")
    private Long idRecompensa;

    @NotBlank
    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @NotBlank
    @Column(name = "descricao", length = 255, nullable = false)
    private String descricao;

    @NotNull
    @PositiveOrZero
    @Column(name = "custo_pontos", nullable = false)
    private BigDecimal custoPontos;

    @NotNull
    @PositiveOrZero
    @Column(name = "estoque", nullable = false)
    private Integer estoque = 0;

    public Recompensas() {}

    public Recompensas(String nome, String descricao, BigDecimal custoPontos, Integer estoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.custoPontos = custoPontos;
        this.estoque = estoque != null ? estoque : 0;
    }

    public boolean isDisponivel() {
        return this.estoque != null && this.estoque > 0;
    }

    public void decrementarEstoque() {
        if (this.estoque != null && this.estoque > 0) {
            this.estoque--;
        }
    }

    public void adicionarEstoque(int quantidade) {
        if (this.estoque == null) {
            this.estoque = 0;
        }
        this.estoque += quantidade;
    }

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

    public BigDecimal getCustoPontos() {
        return custoPontos;
    }

    public void setCustoPontos(BigDecimal custoPontos) {
        this.custoPontos = custoPontos;
    }

    // Métodos alias para compatibilidade com código existente
    public BigDecimal getCusto() {
        return custoPontos;
    }

    public void setCusto(BigDecimal custo) {
        this.custoPontos = custo;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}
