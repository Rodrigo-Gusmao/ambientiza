package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.Respostas.RecompensaResponse;
import com.ambientiza.api.dtos.Respostas.UsuarioResponse;
import com.ambientiza.api.security.UsuarioAutenticado;
import com.ambientiza.api.services.RecompensaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recompensas")
public class RecompensaController {
    private final RecompensaService recompensaService;

    public RecompensaController(RecompensaService recompensaService) {
        this.recompensaService = recompensaService;
    }

    @GetMapping
    public List<RecompensaResponse> listar() {
        return recompensaService.listar();
    }

    @PostMapping("/{idRecompensa}/resgatar")
    public UsuarioResponse resgatar(@AuthenticationPrincipal UsuarioAutenticado auth, @PathVariable Long idRecompensa) {
        return recompensaService.resgatar(auth.idUsuario(), idRecompensa);
    }
}
