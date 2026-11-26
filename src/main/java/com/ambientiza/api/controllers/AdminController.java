package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.Requisicoes.PapelRequest;
import com.ambientiza.api.dtos.Requisicoes.QuizRequest;
import com.ambientiza.api.dtos.Requisicoes.RecompensaRequest;
import com.ambientiza.api.dtos.Requisicoes.TarefaRequest;
import com.ambientiza.api.dtos.Respostas.QuizResponse;
import com.ambientiza.api.dtos.Respostas.RecompensaResponse;
import com.ambientiza.api.dtos.Respostas.TarefaResponse;
import com.ambientiza.api.dtos.Respostas.UsuarioResponse;
import com.ambientiza.api.security.UsuarioAutenticado;
import com.ambientiza.api.services.QuizService;
import com.ambientiza.api.services.RecompensaService;
import com.ambientiza.api.services.TarefaService;
import com.ambientiza.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Acesso restrito a ROLE_ADMIN em SecurityConfig ("/admin/**").
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UsuarioService usuarioService;
    private final RecompensaService recompensaService;
    private final TarefaService tarefaService;
    private final QuizService quizService;

    public AdminController(UsuarioService usuarioService, RecompensaService recompensaService,
                           TarefaService tarefaService, QuizService quizService) {
        this.usuarioService = usuarioService;
        this.recompensaService = recompensaService;
        this.tarefaService = tarefaService;
        this.quizService = quizService;
    }

    @GetMapping("/usuarios")
    public List<UsuarioResponse> usuarios() {
        return usuarioService.listarTodos();
    }

    @PutMapping("/usuarios/{idUsuario}/papel")
    public UsuarioResponse alterarPapel(@AuthenticationPrincipal UsuarioAutenticado auth, @PathVariable Long idUsuario,
                                        @RequestBody @Valid PapelRequest req) {
        return usuarioService.alterarPapel(idUsuario, req.papel(), auth.idUsuario());
    }

    @GetMapping("/recompensas")
    public List<RecompensaResponse> recompensas() {
        return recompensaService.listar();
    }

    @PostMapping("/recompensas")
    @ResponseStatus(HttpStatus.CREATED)
    public RecompensaResponse criarRecompensa(@RequestBody @Valid RecompensaRequest req) {
        return recompensaService.salvar(null, req);
    }

    @PutMapping("/recompensas/{id}")
    public RecompensaResponse atualizarRecompensa(@PathVariable Long id, @RequestBody @Valid RecompensaRequest req) {
        return recompensaService.salvar(id, req);
    }

    @DeleteMapping("/recompensas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerRecompensa(@PathVariable Long id) {
        recompensaService.remover(id);
    }

    @GetMapping("/tarefas")
    public List<TarefaResponse> tarefas() {
        return tarefaService.listarTodas();
    }

    @PostMapping("/tarefas")
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criarTarefa(@RequestBody @Valid TarefaRequest req) {
        return tarefaService.salvar(null, req);
    }

    @PutMapping("/tarefas/{id}")
    public TarefaResponse atualizarTarefa(@PathVariable Long id, @RequestBody @Valid TarefaRequest req) {
        return tarefaService.salvar(id, req);
    }

    @DeleteMapping("/tarefas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerTarefa(@PathVariable Long id) {
        tarefaService.remover(id);
    }

    @GetMapping("/quizzes")
    public List<QuizResponse> quizzes() {
        return quizService.listarTodos();
    }

    @PostMapping("/quizzes")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizResponse criarQuiz(@RequestBody @Valid QuizRequest req) {
        return quizService.criar(req);
    }

    @DeleteMapping("/quizzes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerQuiz(@PathVariable Long id) {
        quizService.remover(id);
    }
}
