package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resgate_recompensas")
public class ResgateRecompensas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resgate")
    private Long idResgate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recompensa", nullable = false)
    private Recompensas recompensa;

    @NotNull
    @PositiveOrZero
    @Column(name = "pontos_gastos", nullable = false)
    private BigDecimal pontosGastos;

    @NotNull
    @Column(name = "data_resgate", nullable = false)
    private LocalDateTime dataResgate;

    public ResgateRecompensas() {
        this.dataResgate = LocalDateTime.now();
    }

    public ResgateRecompensas(Usuarios usuario, Recompensas recompensa, BigDecimal pontosGastos) {
        this.usuario = usuario;
        this.recompensa = recompensa;
        this.pontosGastos = pontosGastos;
        this.dataResgate = LocalDateTime.now();
    }

    public Long getIdResgate() {
        return idResgate;
    }

    public void setIdResgate(Long idResgate) {
        this.idResgate = idResgate;
    }

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

    public BigDecimal getPontosGastos() {
        return pontosGastos;
    }

    public void setPontosGastos(BigDecimal pontosGastos) {
        this.pontosGastos = pontosGastos;
    }

    public LocalDateTime getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(LocalDateTime dataResgate) {
        this.dataResgate = dataResgate;
    }

    // Métodos alias para compatibilidade com o formato original
    public BigDecimal getPontos_gastos() {
        return pontosGastos;
    }

    public void setPontos_gastos(BigDecimal pontosGastos) {
        this.pontosGastos = pontosGastos;
    }
}
