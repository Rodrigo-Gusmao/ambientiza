package com.ambientiza.api.models;

import com.ambientiza.api.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
public class ProgressoTarefas {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgresso;
    @OneToOne @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;
    @OneToOne @JoinColumn(name = "id_tarefa", nullable = false)
    private Tarefas tarefa;
    @NotNull @PastOrPresent
    private LocalDate dataConclusao;
    @NotBlank
    //  ENUM('pendente', 'em_andamento', 'concluído')
    private Status status;

    public ProgressoTarefas() {}

    public Long getIdProgresso() {
        return idProgresso;
    }

    public void setIdProgresso(Long idProgresso) {
        this.idProgresso = idProgresso;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Tarefas getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefas tarefa) {
        this.tarefa = tarefa;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
