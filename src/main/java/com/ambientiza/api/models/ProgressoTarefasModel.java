package com.ambientiza.api.models;

import com.ambientiza.api.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
public class ProgressoTarefasModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgresso;
    @OneToOne @JoinColumn(name = "id_usuario", nullable = false)
    private UsuariosModel usuario;
    @OneToOne @JoinColumn(name = "id_tarefa", nullable = false)
    private TarefasModel tarefa;
    @NotNull @PastOrPresent
    private LocalDate dataConclusao;
    @NotBlank
    //  ENUM('pendente', 'em_andamento', 'concluído')
    private Status status;

    public ProgressoTarefasModel() {}

    public Long getIdProgresso() {
        return idProgresso;
    }

    public void setIdProgresso(Long idProgresso) {
        this.idProgresso = idProgresso;
    }

    public UsuariosModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuariosModel usuario) {
        this.usuario = usuario;
    }

    public TarefasModel getTarefa() {
        return tarefa;
    }

    public void setTarefa(TarefasModel tarefa) {
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
