package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;


import java.time.LocalDate;

@Entity
@Table(name="usuarios")
public class UsuariosModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    @OneToOne(cascade = CascadeType.ALL)
    private SaldoModel saldo;
    @NotBlank
    private String nome;
    @NotBlank
    private String email;
    @NotBlank
    private String senhaHash;
    @NotNull @PastOrPresent
    private LocalDate ultimaAtividade;
    @NotNull @PositiveOrZero
    private Integer ofensivaAtual;
    @NotNull @PastOrPresent
    private LocalDate criadoEm;

    public UsuariosModel() {}

    public LocalDate getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Integer getOfensivaAtual() {
        return ofensivaAtual;
    }

    public void setOfensivaAtual(Integer ofensivaAtual) {
        this.ofensivaAtual = ofensivaAtual;
    }

    public LocalDate getUltimaAtividade() {
        return ultimaAtividade;
    }

    public void setUltimaAtividade(LocalDate ultimaAtividade) {
        this.ultimaAtividade = ultimaAtividade;
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

    public SaldoModel getSaldo() {
        return saldo;
    }

    public void setSaldo(SaldoModel saldo) {
        this.saldo = saldo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
