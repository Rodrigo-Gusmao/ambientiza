package com.ambientiza.api.dtos.recompensa;

import java.math.BigDecimal;

public record RecompensaResponseDTO(
        Long idRecompensa,
        String nome,
        String descricao,
        BigDecimal custo,
        BigDecimal estoque
) {}