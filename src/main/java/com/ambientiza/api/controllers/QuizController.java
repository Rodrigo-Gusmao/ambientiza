package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.quiz.QuizExibicaoResponseDTO;
import com.ambientiza.api.dtos.quiz.QuizRequestDTO;
import com.ambientiza.api.dtos.quiz.QuizSubmissaoRequestDTO;
import com.ambientiza.api.dtos.quiz.QuizTentativaResponseDTO;
import com.ambientiza.api.services.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/quiz")
@Tag(name = "Quiz", description = "Endpoints para gerenciamento e participação nos quizzes ambientais")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo quiz completo com perguntas e alternativas")
    public ResponseEntity<QuizExibicaoResponseDTO> criarQuiz(
            @RequestBody @Valid QuizRequestDTO dto,
            UriComponentsBuilder uriBuilder) {
        QuizExibicaoResponseDTO quizCriado = quizService.criarQuiz(dto);
        URI uri = uriBuilder.path("/quiz/{id}").buildAndExpand(quizCriado.idQuiz()).toUri();
        return ResponseEntity.created(uri).body(quizCriado);
    }

    @GetMapping("/diario")
    @Operation(summary = "Obtém o quiz do dia para o usuário informado caso ainda não tenha jogado")
    public ResponseEntity<QuizExibicaoResponseDTO> obterQuizDoDia(
            @RequestParam Long idUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        LocalDate dataConsulta = (data != null) ? data : LocalDate.now();
        return ResponseEntity.ok(quizService.obterQuizDoDia(idUsuario, dataConsulta));
    }

    @GetMapping("/diario/{idUsuario}")
    @Operation(summary = "Atalho para obter o quiz do dia por ID de usuário na rota")
    public ResponseEntity<QuizExibicaoResponseDTO> obterQuizDoDiaPorPath(
            @PathVariable Long idUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        LocalDate dataConsulta = (data != null) ? data : LocalDate.now();
        return ResponseEntity.ok(quizService.obterQuizDoDia(idUsuario, dataConsulta));
    }

    @PostMapping("/responder")
    @Operation(summary = "Envia a resposta de uma questão do quiz e calcula a pontuação/ofensiva")
    public ResponseEntity<QuizTentativaResponseDTO> enviarResposta(
            @RequestBody @Valid QuizSubmissaoRequestDTO dto) {
        return ResponseEntity.ok(quizService.enviarResposta(dto));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
