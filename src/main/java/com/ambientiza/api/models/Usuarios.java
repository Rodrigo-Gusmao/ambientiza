package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @NotBlank
    @Email
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "senha_hash", length = 255, nullable = false)
    private String senhaHash;

    @NotNull
    @PositiveOrZero
    @Column(name = "saldo", precision = 10, scale = 2, nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Column(name = "ofensiva_atual", nullable = false)
    private Integer ofensivaAtual = 0;

    @PastOrPresent
    @Column(name = "ultima_atividade")
    private LocalDate ultimaAtividade;

    @NotNull
    @PastOrPresent
    @Column(name = "criado_em", nullable = false)
    private LocalDate criadoEm;

    public Usuarios() {
        this.saldo = BigDecimal.ZERO;
        this.ofensivaAtual = 0;
        this.criadoEm = LocalDate.now();
        this.ultimaAtividade = LocalDate.now();
    }

    public Usuarios(String nome, String email, String senhaHash) {
        this();
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    @PrePersist
    public void prePersist() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDate.now();
        }
        if (this.saldo == null) {
            this.saldo = BigDecimal.ZERO;
        }
        if (this.ofensivaAtual == null) {
            this.ofensivaAtual = 0;
        }
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
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

    public LocalDate getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }
}
