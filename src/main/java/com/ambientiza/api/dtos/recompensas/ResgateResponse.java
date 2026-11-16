package com.ambientiza.api.dtos.recompensas;

import com.ambientiza.api.models.ResgateRecompensas;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResgateResponse(
        Long idResgate,
        Long idUsuario,
        String nomeUsuario,
        Long idRecompensa,
        String nomeRecompensa,
        BigDecimal pontosGastos,
        BigDecimal saldoRestante,
        LocalDateTime dataResgate
) {
    public ResgateResponse(ResgateRecompensas resgate, BigDecimal saldoRestante) {
        this(
                resgate.getIdResgate(),
                resgate.getUsuario().getIdUsuario(),
                resgate.getUsuario().getNome(),
                resgate.getRecompensa().getIdRecompensa(),
                resgate.getRecompensa().getNome(),
                resgate.getPontosGastos(),
                saldoRestante,
                resgate.getDataResgate()
        );
    }

    public ResgateResponse(ResgateRecompensas resgate) {
        this(
                resgate.getIdResgate(),
                resgate.getUsuario() != null ? resgate.getUsuario().getIdUsuario() : null,
                resgate.getUsuario() != null ? resgate.getUsuario().getNome() : null,
                resgate.getRecompensa() != null ? resgate.getRecompensa().getIdRecompensa() : null,
                resgate.getRecompensa() != null ? resgate.getRecompensa().getNome() : null,
                resgate.getPontosGastos(),
                resgate.getUsuario() != null ? resgate.getUsuario().getSaldo() : null,
                resgate.getDataResgate()
        );
    }
}
