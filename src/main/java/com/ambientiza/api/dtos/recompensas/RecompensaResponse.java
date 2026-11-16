package com.ambientiza.api.dtos.recompensas;

import com.ambientiza.api.models.Recompensas;

import java.math.BigDecimal;

public record RecompensaResponse(
        Long idRecompensa,
        String nome,
        String descricao,
        BigDecimal custoPontos,
        Integer estoque,
        boolean disponivel
) {
    public RecompensaResponse(Recompensas recompensa) {
        this(
                recompensa.getIdRecompensa(),
                recompensa.getNome(),
                recompensa.getDescricao(),
                recompensa.getCustoPontos(),
                recompensa.getEstoque(),
                recompensa.isDisponivel()
        );
    }
}
