package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.Respostas.TarefaResponse;
import com.ambientiza.api.dtos.Respostas.UsuarioResponse;
import com.ambientiza.api.security.UsuarioAutenticado;
import com.ambientiza.api.services.TarefaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping("/hoje")
    public List<TarefaResponse> hoje(@AuthenticationPrincipal UsuarioAutenticado auth) {
        return tarefaService.listarDoDia(auth.idUsuario());
    }

    @PostMapping("/{idTarefa}/concluir")
    public UsuarioResponse concluir(@AuthenticationPrincipal UsuarioAutenticado auth, @PathVariable Long idTarefa) {
        return tarefaService.concluir(auth.idUsuario(), idTarefa);
    }
}
