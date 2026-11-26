package com.ambientiza.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// Respostas individuais da tentativa de cada quiz; permitem corrigir no servidor pergunta a pergunta.
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_pergunta"}))
public class QuizRespostas {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResposta;
    @ManyToOne(optional = false) @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;
    @ManyToOne(optional = false) @JoinColumn(name = "id_pergunta", nullable = false)
    private QuizPerguntas pergunta;
    @ManyToOne(optional = false) @JoinColumn(name = "id_alternativa", nullable = false)
    private QuizAlternativas alternativa;
    @NotNull
    private Boolean correta;
    @NotNull
    private LocalDate dataResposta;

    public QuizRespostas() {}

    public Long getIdResposta() {
        return idResposta;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public QuizPerguntas getPergunta() {
        return pergunta;
    }

    public void setPergunta(QuizPerguntas pergunta) {
        this.pergunta = pergunta;
    }

    public QuizAlternativas getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(QuizAlternativas alternativa) {
        this.alternativa = alternativa;
    }

    public Boolean getCorreta() {
        return correta;
    }

    public void setCorreta(Boolean correta) {
        this.correta = correta;
    }

    public LocalDate getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(LocalDate dataResposta) {
        this.dataResposta = dataResposta;
    }
}
