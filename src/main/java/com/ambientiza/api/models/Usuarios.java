package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="usuarios")
public class Usuarios {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    @NotNull @PositiveOrZero
    private BigDecimal saldo;
    @NotBlank
    private String nome;
    @NotBlank
    private String email;
    @NotBlank
    private String senhaHash;
    @NotNull @PositiveOrZero
    private Integer ofensivaAtual;


    public Usuarios() {}


    public Integer getOfensivaAtual() {
        return ofensivaAtual;
    }

    public void setOfensivaAtual(Integer ofensivaAtual) {
        this.ofensivaAtual = ofensivaAtual;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
