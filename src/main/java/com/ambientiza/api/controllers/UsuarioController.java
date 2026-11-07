package com.ambientiza.api.controllers;

import com.ambientiza.api.models.UsuariosModel;
import com.ambientiza.api.repositories.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

//    @PostMapping("/cadastro")
//    public ResponseEntity<CadastroUsuario> cadastrarUsuario(@RequestBody @Valid CadastroUsuario cadastroUsuario){
//
//    }

    @GetMapping
    public List<UsuariosModel> findAll() {
        return usuarioRepository.findAll();
    }

}
