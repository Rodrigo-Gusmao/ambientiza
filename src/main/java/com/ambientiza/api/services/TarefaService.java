package com.ambientiza.api.services;

import com.ambientiza.api.dtos.Requisicoes.TarefaRequest;
import com.ambientiza.api.dtos.Respostas.TarefaResponse;
import com.ambientiza.api.dtos.Respostas.UsuarioResponse;
import com.ambientiza.api.enums.Status;
import com.ambientiza.api.enums.Tipo;
import com.ambientiza.api.exceptions.ApiException;
import com.ambientiza.api.models.ProgressoTarefas;
import com.ambientiza.api.models.Tarefas;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.ProgressoTarefasRepository;
import com.ambientiza.api.repositories.TarefasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class TarefaService {
    private final TarefasRepository tarefasRepository;
    private final ProgressoTarefasRepository progressoRepository;
    private final UsuarioService usuarioService;

    public TarefaService(TarefasRepository tarefasRepository, ProgressoTarefasRepository progressoRepository,
                         UsuarioService usuarioService) {
        this.tarefasRepository = tarefasRepository;
        this.progressoRepository = progressoRepository;
        this.usuarioService = usuarioService;
    }

    private static LocalDate inicioPeriodo(Tipo tipo) {
        LocalDate hoje = LocalDate.now();
        return tipo == Tipo.SEMANAL ? hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)) : hoje;
    }

    @Transactional(readOnly = true)
    public List<TarefaResponse> listarDoDia(Long idUsuario) {
        List<ProgressoTarefas> progresso = progressoRepository
                .findByUsuarioIdUsuarioAndDataConclusaoGreaterThanEqual(idUsuario, inicioPeriodo(Tipo.SEMANAL));
        return tarefasRepository.findAll().stream()
                .map(t -> TarefaResponse.de(t, progresso.stream().anyMatch(p ->
                        p.getTarefa().getIdTarefa().equals(t.getIdTarefa())
                                && !p.getDataConclusao().isBefore(inicioPeriodo(t.getTipo())))))
                .toList();
    }

    @Transactional
    public UsuarioResponse concluir(Long idUsuario, Long idTarefa) {
        Usuarios usuario = usuarioService.obterParaAtualizar(idUsuario);
        Tarefas tarefa = obter(idTarefa);
        if (progressoRepository.existsByUsuarioIdUsuarioAndTarefaIdTarefaAndDataConclusaoGreaterThanEqual(
                idUsuario, idTarefa, inicioPeriodo(tarefa.getTipo()))) {
            throw ApiException.conflito("Essa missão já foi concluída neste período.");
        }

        ProgressoTarefas p = new ProgressoTarefas();
        p.setUsuario(usuario);
        p.setTarefa(tarefa);
        p.setDataConclusao(LocalDate.now());
        p.setStatus(Status.CONCLUIDO);
        progressoRepository.save(p);

        usuario.setSaldo(usuario.getSaldo() + tarefa.getPontosRecompensa());
        usuarioService.registrarAtividade(usuario);
        return usuarioService.paraResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<TarefaResponse> listarTodas() {
        return tarefasRepository.findAll().stream().map(t -> TarefaResponse.de(t, false)).toList();
    }

    @Transactional
    public TarefaResponse salvar(Long idTarefa, TarefaRequest req) {
        Tarefas t = idTarefa == null ? new Tarefas() : obter(idTarefa);
        t.setTitulo(req.titulo().trim());
        t.setDescricao(req.descricao().trim());
        t.setPontosRecompensa(req.pontosRecompensa());
        t.setTipo(req.tipo());
        return TarefaResponse.de(tarefasRepository.save(t), false);
    }

    @Transactional
    public void remover(Long idTarefa) {
        Tarefas t = obter(idTarefa);
        if (progressoRepository.existsByTarefaIdTarefa(idTarefa)) {
            throw ApiException.conflito("Essa tarefa já foi concluída por usuários e não pode ser removida.");
        }
        tarefasRepository.delete(t);
    }

    private Tarefas obter(Long idTarefa) {
        return tarefasRepository.findById(idTarefa)
                .orElseThrow(() -> ApiException.naoEncontrado("Tarefa não encontrada."));
    }
}
