package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Entity
public class ResgateRecompensas {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResgate;
    @ManyToOne(optional = false) @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;
    @ManyToOne(optional = false) @JoinColumn(name = "id_recompensa", nullable = false)
    private Recompensas recompensa;
    @NotNull @PositiveOrZero
    private Integer pontosGastos;
    @NotNull
    private LocalDate dataResgate;

    public ResgateRecompensas() {}

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Recompensas getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(Recompensas recompensa) {
        this.recompensa = recompensa;
    }

    public Long getIdResgate() {
        return idResgate;
    }

    public void setIdResgate(Long idResgate) {
        this.idResgate = idResgate;
    }

    public Integer getPontosGastos() {
        return pontosGastos;
    }

    public void setPontosGastos(Integer pontosGastos) {
        this.pontosGastos = pontosGastos;
    }

    public LocalDate getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(LocalDate dataResgate) {
        this.dataResgate = dataResgate;
    }
}
