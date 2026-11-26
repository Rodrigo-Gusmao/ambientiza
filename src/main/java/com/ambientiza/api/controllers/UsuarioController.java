package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.Respostas.EstatisticasResponse;
import com.ambientiza.api.dtos.Respostas.ResgateResponse;
import com.ambientiza.api.dtos.Respostas.UsuarioResponse;
import com.ambientiza.api.security.UsuarioAutenticado;
import com.ambientiza.api.services.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public UsuarioResponse me(@AuthenticationPrincipal UsuarioAutenticado auth) {
        return usuarioService.buscar(auth.idUsuario());
    }

    @GetMapping("/me/estatisticas")
    public EstatisticasResponse estatisticas(@AuthenticationPrincipal UsuarioAutenticado auth) {
        return usuarioService.estatisticas(auth.idUsuario());
    }

    @GetMapping("/me/resgates")
    public List<ResgateResponse> resgates(@AuthenticationPrincipal UsuarioAutenticado auth) {
        return usuarioService.resgates(auth.idUsuario());
    }
}
