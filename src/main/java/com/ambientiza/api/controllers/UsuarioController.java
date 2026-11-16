package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.usuarios.*;
import com.ambientiza.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastroUsuario dto) {
        UsuarioResponse resposta = usuarioService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@Valid @RequestBody LoginUsuario dto) {
        UsuarioResponse resposta = usuarioService.login(dto);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public List<ListarNomes> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/todos")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarUsuario dto
    ) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable Long id,
            @Valid @RequestBody AlterarSenha dto
    ) {
        usuarioService.alterarSenha(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/ofensiva")
    public ResponseEntity<OfensivaResponse> consultarOfensiva(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.consultarOfensiva(id));
    }

    @PostMapping("/{id}/ofensiva")
    public ResponseEntity<OfensivaResponse> registrarAtividade(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.registrarAtividadeEAtualizarOfensiva(id));
    }

    @PostMapping("/{id}/pontos/adicionar")
    public ResponseEntity<UsuarioResponse> adicionarPontos(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPontos dto
    ) {
        return ResponseEntity.ok(usuarioService.adicionarPontos(id, dto.pontos()));
    }

    @PostMapping("/{id}/pontos/debitar")
    public ResponseEntity<UsuarioResponse> debitarPontos(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPontos dto
    ) {
        return ResponseEntity.ok(usuarioService.debitarPontos(id, dto.pontos()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
