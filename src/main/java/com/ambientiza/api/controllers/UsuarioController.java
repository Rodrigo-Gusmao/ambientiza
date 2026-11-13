package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.usuarios.ListarNomes;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.UsuarioRepository;
import com.ambientiza.api.services.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<ListarNomes> findAll() {
        return usuarioService.findAll();
    }

}
