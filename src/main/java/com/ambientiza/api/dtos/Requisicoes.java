package com.ambientiza.api.dtos;

import com.ambientiza.api.enums.NivelDificuldade;
import com.ambientiza.api.enums.Papel;
import com.ambientiza.api.enums.Temas;
import com.ambientiza.api.enums.Tipo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public final class Requisicoes {
    private Requisicoes() {}

    public record RespostaQuizRequest(@NotNull Long idPergunta, @NotNull Long idAlternativa) {}

    public record RecompensaRequest(
            @NotBlank @Size(max = 120) String nome,
            @NotBlank @Size(max = 255) String descricao,
            @NotNull @PositiveOrZero Integer custo,
            @NotNull @PositiveOrZero Integer estoque) {}

    public record TarefaRequest(
            @NotBlank @Size(max = 120) String titulo,
            @NotBlank @Size(max = 255) String descricao,
            @NotNull @PositiveOrZero Integer pontosRecompensa,
            @NotNull Tipo tipo) {}

    public record AlternativaRequest(@NotBlank @Size(max = 255) String texto, boolean correta) {}

    public record PerguntaRequest(
            @NotBlank @Size(max = 255) String enunciado,
            @NotNull NivelDificuldade nivelDificuldade,
            @NotNull @Size(min = 2, max = 6) List<@Valid AlternativaRequest> alternativas) {}

    public record QuizRequest(
            @NotBlank @Size(max = 120) String titulo,
            @NotNull Temas tema,
            @NotNull LocalDate dataDisponivel,
            @NotNull @Size(min = 1, max = 20) List<@Valid PerguntaRequest> perguntas) {}

    public record PapelRequest(@NotNull Papel papel) {}
}
