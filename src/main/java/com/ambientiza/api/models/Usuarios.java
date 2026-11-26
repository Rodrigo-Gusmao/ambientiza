package com.ambientiza.api.models;

import com.ambientiza.api.enums.Papel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name="usuarios")
public class Usuarios {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    @NotNull @PositiveOrZero
    private Integer saldo = 0;
    @NotBlank
    private String nome;
    @NotBlank @Email
    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank
    private String senhaHash;
    @NotNull @Enumerated(EnumType.STRING)
    private Papel papel = Papel.USUARIO;
    // null enquanto o usuário não tiver nenhuma atividade
    @PastOrPresent
    private LocalDate ultimaAtividade;
    @NotNull @PositiveOrZero
    private Integer ofensivaAtual = 0;
    @NotNull @PastOrPresent
    private LocalDate criadoEm;

    public Usuarios() {}

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

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
