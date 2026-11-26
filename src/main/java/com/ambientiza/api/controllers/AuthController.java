package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.Respostas.LoginResponse;
import com.ambientiza.api.dtos.usuarios.Cadastro;
import com.ambientiza.api.dtos.usuarios.LoginUsuario;
import com.ambientiza.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginUsuario login) {
        return usuarioService.login(login);
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse cadastro(@RequestBody @Valid Cadastro cadastro) {
        return usuarioService.cadastrar(cadastro);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(Authentication auth) {
        usuarioService.logout((String) auth.getCredentials());
    }
}
