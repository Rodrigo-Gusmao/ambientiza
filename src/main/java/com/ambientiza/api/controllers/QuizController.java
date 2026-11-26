package com.ambientiza.api.controllers;

import com.ambientiza.api.dtos.Requisicoes.RespostaQuizRequest;
import com.ambientiza.api.dtos.Respostas.FinalizarQuizResponse;
import com.ambientiza.api.dtos.Respostas.QuizDisponivelResponse;
import com.ambientiza.api.dtos.Respostas.QuizDoDiaResponse;
import com.ambientiza.api.dtos.Respostas.ResultadoRespostaResponse;
import com.ambientiza.api.security.UsuarioAutenticado;
import com.ambientiza.api.services.QuizService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public List<QuizDisponivelResponse> disponiveis(@AuthenticationPrincipal UsuarioAutenticado auth) {
        return quizService.listarDisponiveis(auth.idUsuario());
    }

    @GetMapping("/{idQuiz}")
    public QuizDoDiaResponse buscar(@AuthenticationPrincipal UsuarioAutenticado auth, @PathVariable Long idQuiz) {
        return quizService.buscar(auth.idUsuario(), idQuiz);
    }

    @PostMapping("/respostas")
    public ResultadoRespostaResponse responder(@AuthenticationPrincipal UsuarioAutenticado auth,
                                               @RequestBody @Valid RespostaQuizRequest req) {
        return quizService.responder(auth.idUsuario(), req);
    }

    @PostMapping("/{idQuiz}/finalizar")
    public FinalizarQuizResponse finalizar(@AuthenticationPrincipal UsuarioAutenticado auth, @PathVariable Long idQuiz) {
        return quizService.finalizar(auth.idUsuario(), idQuiz);
    }
}
