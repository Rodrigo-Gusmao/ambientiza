package com.ambientiza.api.services;

import com.ambientiza.api.dtos.Requisicoes.AlternativaRequest;
import com.ambientiza.api.dtos.Requisicoes.PerguntaRequest;
import com.ambientiza.api.dtos.Requisicoes.QuizRequest;
import com.ambientiza.api.dtos.Requisicoes.RespostaQuizRequest;
import com.ambientiza.api.dtos.Respostas.*;
import com.ambientiza.api.exceptions.ApiException;
import com.ambientiza.api.models.*;
import com.ambientiza.api.repositories.QuizAlternativasRepository;
import com.ambientiza.api.repositories.QuizRepository;
import com.ambientiza.api.repositories.QuizRespostasRepository;
import com.ambientiza.api.repositories.QuizTentativaDiariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuizService {
    // Quizzes de dias anteriores continuam jogáveis por esse número de dias, valendo metade dos pontos.
    public static final int DIAS_TOLERANCIA = 7;
    private final QuizRepository quizRepository;
    private final QuizAlternativasRepository alternativasRepository;
    private final QuizRespostasRepository respostasRepository;
    private final QuizTentativaDiariaRepository tentativaRepository;
    private final UsuarioService usuarioService;

    public QuizService(QuizRepository quizRepository, QuizAlternativasRepository alternativasRepository,
                       QuizRespostasRepository respostasRepository, QuizTentativaDiariaRepository tentativaRepository,
                       UsuarioService usuarioService) {
        this.quizRepository = quizRepository;
        this.alternativasRepository = alternativasRepository;
        this.respostasRepository = respostasRepository;
        this.tentativaRepository = tentativaRepository;
        this.usuarioService = usuarioService;
    }

    private Quiz quizDisponivel(Long idQuiz) {
        Quiz quiz = quizRepository.findById(idQuiz)
                .orElseThrow(() -> ApiException.naoEncontrado("Quiz não encontrado."));
        if (quiz.getDataDisponivel().isAfter(LocalDate.now())) {
            throw ApiException.naoEncontrado("Esse quiz ainda não está disponível.");
        }
        return quiz;
    }

    private static boolean expirado(Quiz quiz) {
        return quiz.getDataDisponivel().isBefore(LocalDate.now().minusDays(DIAS_TOLERANCIA));
    }

    private static boolean valeMetade(Quiz quiz) {
        return quiz.getDataDisponivel().isBefore(LocalDate.now());
    }

    private static ApiException quizExpirado() {
        return ApiException.invalido("Esse quiz expirou. Quizzes ficam disponíveis por até "
                + DIAS_TOLERANCIA + " dias após a data dele.");
    }

    private List<QuizRespostas> respostasDoQuiz(Long idUsuario, Quiz quiz) {
        return respostasRepository.findByUsuarioIdUsuarioAndPerguntaQuizIdQuiz(idUsuario, quiz.getIdQuiz());
    }

    private TentativaResponse tentativaDoQuiz(Long idUsuario, Quiz quiz) {
        return tentativaRepository.findByUsuarioIdUsuarioAndQuizIdQuiz(idUsuario, quiz.getIdQuiz())
                .map(TentativaResponse::de)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<QuizDisponivelResponse> listarDisponiveis(Long idUsuario) {
        LocalDate hoje = LocalDate.now();
        return quizRepository.findByDataDisponivelBetweenOrderByDataDisponivelDescIdQuizDesc(
                        hoje.minusDays(DIAS_TOLERANCIA), hoje).stream()
                .map(q -> new QuizDisponivelResponse(QuizResponse.de(q), respostasDoQuiz(idUsuario, q).size(),
                        tentativaDoQuiz(idUsuario, q), valeMetade(q)))
                .toList();
    }

    @Transactional(readOnly = true)
    public QuizDoDiaResponse buscar(Long idUsuario, Long idQuiz) {
        Quiz quiz = quizDisponivel(idQuiz);
        TentativaResponse tentativa = tentativaDoQuiz(idUsuario, quiz);
        // Quiz expirado só pode ser aberto para ver o resultado de quem já jogou.
        if (tentativa == null && expirado(quiz)) {
            throw quizExpirado();
        }
        List<Long> respondidas = respostasDoQuiz(idUsuario, quiz).stream()
                .map(r -> r.getPergunta().getIdPergunta())
                .toList();
        return new QuizDoDiaResponse(QuizResponse.de(quiz),
                quiz.getPerguntas().stream().map(PerguntaResponse::de).toList(),
                respondidas, tentativa, valeMetade(quiz));
    }

    @Transactional
    public ResultadoRespostaResponse responder(Long idUsuario, RespostaQuizRequest req) {
        Usuarios usuario = usuarioService.obterParaAtualizar(idUsuario);
        QuizAlternativas alternativa = alternativasRepository.findById(req.idAlternativa())
                .orElseThrow(() -> ApiException.naoEncontrado("Alternativa não encontrada."));
        QuizPerguntas pergunta = alternativa.getQuizPerguntas();
        if (!pergunta.getIdPergunta().equals(req.idPergunta())) {
            throw ApiException.invalido("Alternativa não pertence a essa pergunta.");
        }
        Quiz quiz = quizDisponivel(pergunta.getQuiz().getIdQuiz());
        if (expirado(quiz)) {
            throw quizExpirado();
        }
        if (tentativaRepository.findByUsuarioIdUsuarioAndQuizIdQuiz(idUsuario, quiz.getIdQuiz()).isPresent()) {
            throw ApiException.conflito("Você já concluiu esse quiz.");
        }
        if (respostasRepository.existsByUsuarioIdUsuarioAndPerguntaIdPergunta(idUsuario, pergunta.getIdPergunta())) {
            throw ApiException.conflito("Essa pergunta já foi respondida.");
        }

        QuizRespostas resposta = new QuizRespostas();
        resposta.setUsuario(usuario);
        resposta.setPergunta(pergunta);
        resposta.setAlternativa(alternativa);
        resposta.setCorreta(Boolean.TRUE.equals(alternativa.getCorreta()));
        resposta.setDataResposta(LocalDate.now());
        respostasRepository.save(resposta);

        Long idCorreta = pergunta.getAlternativas().stream()
                .filter(a -> Boolean.TRUE.equals(a.getCorreta()))
                .map(QuizAlternativas::getIdQuizAlternativas)
                .findFirst()
                .orElse(null);
        return new ResultadoRespostaResponse(resposta.getCorreta(), idCorreta);
    }

    @Transactional
    public FinalizarQuizResponse finalizar(Long idUsuario, Long idQuiz) {
        Usuarios usuario = usuarioService.obterParaAtualizar(idUsuario);
        Quiz quiz = quizDisponivel(idQuiz);
        var existente = tentativaRepository.findByUsuarioIdUsuarioAndQuizIdQuiz(idUsuario, idQuiz);
        if (existente.isPresent()) {
            return new FinalizarQuizResponse(TentativaResponse.de(existente.get()), usuarioService.paraResponse(usuario));
        }

        List<QuizRespostas> respostas = respostasDoQuiz(idUsuario, quiz);
        if (respostas.size() < quiz.getPerguntas().size()) {
            throw ApiException.invalido("Responda todas as perguntas antes de finalizar.");
        }

        List<QuizRespostas> corretas = respostas.stream().filter(QuizRespostas::getCorreta).toList();
        int acertos = corretas.size();
        // Pontuação por dificuldade: FACIL = 10, MEDIO = 30, DIFICIL = 50 (ver NivelDificuldade).
        int pontos = corretas.stream()
                .mapToInt(r -> r.getPergunta().getNivelDificuldade().getPontos())
                .sum();
        // Feito no dia do quiz: pontuação integral. Feito em dia posterior (até DIAS_TOLERANCIA): metade.
        if (valeMetade(quiz)) {
            pontos = pontos / 2;
        }
        QuizTentativaDiaria tentativa = new QuizTentativaDiaria();
        tentativa.setQuiz(quiz);
        tentativa.setUsuario(usuario);
        tentativa.setDataJogada(LocalDate.now());
        tentativa.setAcerto(acertos);
        tentativa.setPontosGanhos(pontos);
        tentativaRepository.save(tentativa);

        usuario.setSaldo(usuario.getSaldo() + tentativa.getPontosGanhos());
        usuarioService.registrarAtividade(usuario);
        return new FinalizarQuizResponse(TentativaResponse.de(tentativa), usuarioService.paraResponse(usuario));
    }

    @Transactional(readOnly = true)
    public List<QuizResponse> listarTodos() {
        return quizRepository.findAllByOrderByDataDisponivelDesc().stream().map(QuizResponse::de).toList();
    }

    @Transactional
    public QuizResponse criar(QuizRequest req) {
        Quiz quiz = new Quiz();
        quiz.setTitulo(req.titulo().trim());
        quiz.setTema(req.tema());
        quiz.setDataDisponivel(req.dataDisponivel());

        for (PerguntaRequest pr : req.perguntas()) {
            long corretas = pr.alternativas().stream().filter(AlternativaRequest::correta).count();
            if (corretas != 1) {
                throw ApiException.invalido("A pergunta \"" + pr.enunciado() + "\" deve ter exatamente uma alternativa correta.");
            }
            Set<String> textos = pr.alternativas().stream().map(a -> a.texto().trim().toLowerCase()).collect(Collectors.toSet());
            if (textos.size() != pr.alternativas().size()) {
                throw ApiException.invalido("A pergunta \"" + pr.enunciado() + "\" tem alternativas repetidas.");
            }

            QuizPerguntas pergunta = new QuizPerguntas();
            pergunta.setEnunciado(pr.enunciado().trim());
            pergunta.setNivelDificuldade(pr.nivelDificuldade());
            for (AlternativaRequest ar : pr.alternativas()) {
                QuizAlternativas alt = new QuizAlternativas();
                alt.setTexto(ar.texto().trim());
                alt.setCorreta(ar.correta());
                pergunta.adicionarAlternativa(alt);
            }
            quiz.adicionarPergunta(pergunta);
        }
        return QuizResponse.de(quizRepository.save(quiz));
    }

    @Transactional
    public void remover(Long idQuiz) {
        Quiz quiz = quizRepository.findById(idQuiz)
                .orElseThrow(() -> ApiException.naoEncontrado("Quiz não encontrado."));
        if (tentativaRepository.existsByQuizIdQuiz(idQuiz) || respostasRepository.existsByPerguntaQuizIdQuiz(idQuiz)) {
            throw ApiException.conflito("Esse quiz já foi jogado e não pode ser removido.");
        }
        quizRepository.delete(quiz);
    }
}
