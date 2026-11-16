package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.recompensas.*;
import com.ambientiza.api.services.RecompensaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recompensas")
public class RecompensaController {

    private final RecompensaService recompensaService;

    public RecompensaController(RecompensaService recompensaService) {
        this.recompensaService = recompensaService;
    }

    @PostMapping
    public ResponseEntity<RecompensaResponse> cadastrar(@Valid @RequestBody CadastroRecompensa dto) {
        RecompensaResponse resposta = recompensaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<RecompensaResponse>> listarTodas() {
        return ResponseEntity.ok(recompensaService.listarTodas());
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<RecompensaResponse>> listarDisponiveis() {
        return ResponseEntity.ok(recompensaService.listarDisponiveis());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecompensaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recompensaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecompensaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarRecompensa dto
    ) {
        return ResponseEntity.ok(recompensaService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/estoque")
    public ResponseEntity<RecompensaResponse> adicionarEstoque(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarEstoque dto
    ) {
        return ResponseEntity.ok(recompensaService.adicionarEstoque(id, dto.quantidade()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recompensaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resgatar")
    public ResponseEntity<ResgateResponse> resgatar(@Valid @RequestBody ResgateRequest dto) {
        ResgateResponse resposta = recompensaService.resgatar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/resgates/usuario/{idUsuario}")
    public ResponseEntity<List<ResgateResponse>> listarHistoricoUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(recompensaService.listarHistoricoUsuario(idUsuario));
    }

    @GetMapping("/resgates")
    public ResponseEntity<List<ResgateResponse>> listarTodosResgates() {
        return ResponseEntity.ok(recompensaService.listarTodosResgates());
    }

    @GetMapping("/resgates/{idResgate}")
    public ResponseEntity<ResgateResponse> buscarResgatePorId(@PathVariable Long idResgate) {
        return ResponseEntity.ok(recompensaService.buscarResgatePorId(idResgate));
    }
}
