package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


import java.math.BigDecimal;

@Entity
@Table(name="saldos")
public class SaldoModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSaldo;
    @NotNull @PositiveOrZero
    private BigDecimal saldo;
    @OneToOne @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private UsuariosModel usuario;

    public SaldoModel() {}

    public Long getIdSaldo() {
        return idSaldo;
    }

    public void setIdSaldo(Long idSaldo) {
        this.idSaldo = idSaldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public UsuariosModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuariosModel usuario) {
        this.usuario = usuario;
    }
}
