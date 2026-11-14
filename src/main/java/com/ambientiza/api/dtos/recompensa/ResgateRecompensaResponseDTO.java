package com.ambientiza.api.dtos.recompensa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResgateRecompensaResponseDTO(
        Long idResgate,
        String nomeUsuario,
        String nomeRecompensa,
        BigDecimal pontosGastos,
        LocalDateTime dataResgate,
        BigDecimal saldoRestante
) {}