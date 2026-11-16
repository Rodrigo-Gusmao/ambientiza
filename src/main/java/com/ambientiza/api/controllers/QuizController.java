package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.quiz.*;
import com.ambientiza.api.enums.Temas;
import com.ambientiza.api.models.QuizTentativaDiaria;
import com.ambientiza.api.services.QuizService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<QuizResponse> cadastrar(@Valid @RequestBody CadastroQuiz dto) {
        QuizResponse resposta = quizService.cadastrarQuiz(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/hoje")
    public ResponseEntity<QuizResponse> buscarQuizDoDia(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return ResponseEntity.ok(quizService.buscarQuizDoDia(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<QuizResponse>> listarTodos() {
        return ResponseEntity.ok(quizService.listarTodos());
    }

    @GetMapping("/tema/{tema}")
    public ResponseEntity<List<QuizResponse>> listarPorTema(@PathVariable Temas tema) {
        return ResponseEntity.ok(quizService.listarPorTema(tema));
    }

    @PostMapping("/responder")
    public ResponseEntity<TentativaQuizResponse> responderQuiz(@Valid @RequestBody ResponderQuizRequest dto) {
        TentativaQuizResponse resposta = quizService.responderQuiz(dto);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}/status/usuario/{idUsuario}")
    public ResponseEntity<StatusQuizDiarioResponse> verificarStatusDiario(
            @PathVariable Long id,
            @PathVariable Long idUsuario
    ) {
        return ResponseEntity.ok(quizService.verificarStatusDiario(idUsuario, id));
    }

    @GetMapping("/tentativas/usuario/{idUsuario}")
    public ResponseEntity<List<QuizTentativaDiaria>> listarTentativasUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(quizService.listarTentativasUsuario(idUsuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        quizService.deletarQuiz(id);
        return ResponseEntity.noContent().build();
    }
}
