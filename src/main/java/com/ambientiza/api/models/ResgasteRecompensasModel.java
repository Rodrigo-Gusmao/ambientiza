package com.ambientiza.api.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ResgasteRecompensasModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResgate;
    @OneToOne @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private UsuariosModel usuario;
    @ManyToOne @JoinColumn(name = "id_recompensa", nullable = false)
    private RecompensasModel recompensa;
    private BigDecimal pontos_gastos;
    private BigDecimal data_resgate;

    public ResgasteRecompensasModel() {}

    public UsuariosModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuariosModel usuario) {
        this.usuario = usuario;
    }

    public RecompensasModel getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(RecompensasModel recompensa) {
        this.recompensa = recompensa;
    }

    public Long getIdResgate() {
        return idResgate;
    }

    public void setIdResgate(Long idResgate) {
        this.idResgate = idResgate;
    }

    public BigDecimal getPontos_gastos() {
        return pontos_gastos;
    }

    public void setPontos_gastos(BigDecimal pontos_gastos) {
        this.pontos_gastos = pontos_gastos;
    }

    public BigDecimal getData_resgate() {
        return data_resgate;
    }

    public void setData_resgate(BigDecimal data_resgate) {
        this.data_resgate = data_resgate;
    }
}
