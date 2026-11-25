package com.ambientiza.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ambientiza.api.dtos.recompensas.AtualizarEstoque;
import com.ambientiza.api.dtos.recompensas.AtualizarRecompensa;
import com.ambientiza.api.dtos.recompensas.CadastroRecompensa;
import com.ambientiza.api.dtos.recompensas.RecompensaResponse;
import com.ambientiza.api.dtos.recompensas.ResgateRequest;
import com.ambientiza.api.dtos.recompensas.ResgateResponse;
import com.ambientiza.api.services.RecompensaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recompensa")
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
