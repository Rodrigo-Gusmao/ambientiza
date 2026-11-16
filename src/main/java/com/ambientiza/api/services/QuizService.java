package com.ambientiza.api.services;

import com.ambientiza.api.dtos.quiz.*;
import com.ambientiza.api.dtos.usuarios.OfensivaResponse;
import com.ambientiza.api.dtos.usuarios.UsuarioResponse;
import com.ambientiza.api.enums.NivelDificuldade;
import com.ambientiza.api.enums.Temas;
import com.ambientiza.api.exceptions.RecursoNaoEncontradoException;
import com.ambientiza.api.exceptions.RegraDeNegocioException;
import com.ambientiza.api.exceptions.TentativaJaRealizadaException;
import com.ambientiza.api.models.Quiz;
import com.ambientiza.api.models.QuizAlternativas;
import com.ambientiza.api.models.QuizPerguntas;
import com.ambientiza.api.models.QuizTentativaDiaria;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizPerguntasRepository quizPerguntasRepository;
    private final QuizAlternativasRepository quizAlternativasRepository;
    private final QuizTentativaDiariaRepository quizTentativaDiariaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public QuizService(
            QuizRepository quizRepository,
            QuizPerguntasRepository quizPerguntasRepository,
            QuizAlternativasRepository quizAlternativasRepository,
            QuizTentativaDiariaRepository quizTentativaDiariaRepository,
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService
    ) {
        this.quizRepository = quizRepository;
        this.quizPerguntasRepository = quizPerguntasRepository;
        this.quizAlternativasRepository = quizAlternativasRepository;
        this.quizTentativaDiariaRepository = quizTentativaDiariaRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Cadastra um novo Quiz completo com suas respectivas Perguntas e Alternativas.
     * Valida que cada pergunta tenha exatamente uma alternativa correta.
     */
    @Transactional
    public QuizResponse cadastrarQuiz(CadastroQuiz dto) {
        Quiz quiz = new Quiz(dto.titulo().trim(), dto.tema(), dto.dataDisponivel());

        for (CadastroPergunta perguntaDto : dto.perguntas()) {
            long totalCorretas = perguntaDto.alternativas().stream()
                    .filter(CadastroAlternativa::correta)
                    .count();

            if (totalCorretas != 1) {
                throw new RegraDeNegocioException("A pergunta '" + perguntaDto.enunciado() +
                        "' deve conter exatamente uma alternativa correta. Quantidade informada: " + totalCorretas);
            }

            QuizPerguntas pergunta = new QuizPerguntas(quiz, perguntaDto.enunciado().trim(), perguntaDto.nivelDificuldade());

            for (CadastroAlternativa alternativaDto : perguntaDto.alternativas()) {
                QuizAlternativas alternativa = new QuizAlternativas(
                        pergunta,
                        alternativaDto.texto().trim(),
                        alternativaDto.correta()
                );
                pergunta.adicionarAlternativa(alternativa);
            }

            quiz.adicionarPergunta(pergunta);
        }

        Quiz salvo = quizRepository.save(quiz);
        return new QuizResponse(salvo);
    }

    /**
     * Retorna o Quiz diário configurado para a data especificada (ou hoje por padrão).
     */
    @Transactional(readOnly = true)
    public QuizResponse buscarQuizDoDia(LocalDate data) {
        LocalDate dataConsulta = data != null ? data : LocalDate.now();

        return quizRepository.findFirstByDataDisponivelOrderByDataDisponivelDesc(dataConsulta)
                .map(QuizResponse::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum quiz disponível para a data: " + dataConsulta));
    }

    /**
     * Busca um Quiz pelo ID com suas perguntas e alternativas.
     */
    @Transactional(readOnly = true)
    public QuizResponse buscarPorId(Long idQuiz) {
        return new QuizResponse(buscarEntidadePorId(idQuiz));
    }

    /**
     * Busca a entidade Quiz diretamente para uso interno.
     */
    @Transactional(readOnly = true)
    public Quiz buscarEntidadePorId(Long idQuiz) {
        return quizRepository.findById(idQuiz)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quiz não encontrado com o ID: " + idQuiz));
    }

    /**
     * Lista todos os Quizzes cadastrados.
     */
    @Transactional(readOnly = true)
    public List<QuizResponse> listarTodos() {
        return quizRepository.findAll()
                .stream()
                .map(QuizResponse::new)
                .toList();
    }

    /**
     * Filtra quizzes por tema ambiental.
     */
    @Transactional(readOnly = true)
    public List<QuizResponse> listarPorTema(Temas tema) {
        return quizRepository.findByTema(tema)
                .stream()
                .map(QuizResponse::new)
                .toList();
    }

    /**
     * Processa a resposta do usuário a um Quiz Diário.
     * Regras Obrigatórias da A3:
     * 1. Controle de Tentativas: Restrição estrita de 1 tentativa por dia por utilizador para garantir a integridade da pontuação.
     * 2. Cálculo de pontos conforme dificuldade (Fácil: 10, Médio: 20, Difícil: 30).
     * 3. Atualização transacional do saldo de pontos do usuário.
     * 4. Registro de atividade diária para progressão de ofensiva (streak).
     * 5. Registro auditável da tentativa diária.
     */
    @Transactional
    public TentativaQuizResponse responderQuiz(ResponderQuizRequest dto) {
        LocalDate hoje = LocalDate.now();

        Usuarios usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + dto.idUsuario()));

        Quiz quiz = buscarEntidadePorId(dto.idQuiz());

        // 1. Controle estrito de tentativa diária
        if (quizTentativaDiariaRepository.existsByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(usuario.getIdUsuario(), quiz.getIdQuiz(), hoje)) {
            throw new TentativaJaRealizadaException(
                    "Você já respondeu a este quiz hoje (" + hoje + "). O sistema permite apenas uma tentativa diária para garantir a integridade dos pontos."
            );
        }

        Map<Long, Long> mapaRespostas = dto.respostas().stream()
                .collect(Collectors.toMap(RespostaItem::idPergunta, RespostaItem::idAlternativa, (r1, r2) -> r1));

        List<QuizPerguntas> perguntas = quiz.getPerguntas();
        if (perguntas.isEmpty()) {
            perguntas = quizPerguntasRepository.findByQuizIdQuiz(quiz.getIdQuiz());
        }

        int totalAcertos = 0;
        int pontosTotais = 0;
        List<ResultadoPergunta> detalhes = new ArrayList<>();

        for (QuizPerguntas pergunta : perguntas) {
            Long idAlternativaEscolhida = mapaRespostas.get(pergunta.getIdPergunta());
            if (idAlternativaEscolhida == null) {
                throw new RegraDeNegocioException("Resposta ausente para a pergunta: " + pergunta.getEnunciado());
            }

            QuizAlternativas alternativaEscolhida = quizAlternativasRepository.findById(idAlternativaEscolhida)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Alternativa não encontrada com o ID: " + idAlternativaEscolhida));

            if (!alternativaEscolhida.getQuizPerguntas().getIdPergunta().equals(pergunta.getIdPergunta())) {
                throw new RegraDeNegocioException("A alternativa informada não pertence à pergunta ID: " + pergunta.getIdPergunta());
            }

            QuizAlternativas alternativaCorreta = quizAlternativasRepository
                    .findByQuizPerguntasIdPerguntaAndCorretaTrue(pergunta.getIdPergunta())
                    .orElseThrow(() -> new RegraDeNegocioException("Pergunta sem alternativa correta configurada: " + pergunta.getIdPergunta()));

            boolean acertou = Boolean.TRUE.equals(alternativaEscolhida.getCorreta());
            int pontosPergunta = 0;

            if (acertou) {
                totalAcertos++;
                pontosPergunta = calcularPontosPorDificuldade(pergunta.getNivelDificuldade());
                pontosTotais += pontosPergunta;
            }

            detalhes.add(new ResultadoPergunta(
                    pergunta.getIdPergunta(),
                    pergunta.getEnunciado(),
                    idAlternativaEscolhida,
                    alternativaCorreta.getIdAlternativa(),
                    acertou,
                    pontosPergunta
            ));
        }

        boolean acertoGeral = totalAcertos > 0;

        // 2. Transação: crédito de pontos no saldo
        if (pontosTotais > 0) {
            usuarioService.adicionarPontos(usuario.getIdUsuario(), BigDecimal.valueOf(pontosTotais));
        }

        // 3. Transação: registro de atividade diária e atualização de streak
        OfensivaResponse ofensiva = usuarioService.registrarAtividadeEAtualizarOfensiva(usuario.getIdUsuario());

        // 4. Salva a tentativa diária no banco
        QuizTentativaDiaria tentativa = new QuizTentativaDiaria(quiz, usuario, acertoGeral, pontosTotais);
        QuizTentativaDiaria tentativaSalva = quizTentativaDiariaRepository.save(tentativa);

        // Recarrega saldo atualizado
        Usuarios usuarioAtualizado = usuarioRepository.findById(usuario.getIdUsuario()).orElse(usuario);

        String feedback = gerarMensagemFeedback(totalAcertos, perguntas.size(), pontosTotais, ofensiva.ofensivaAtual());

        return new TentativaQuizResponse(
                tentativaSalva.getIdTentativa(),
                quiz.getIdQuiz(),
                quiz.getTitulo(),
                usuario.getIdUsuario(),
                usuario.getNome(),
                hoje,
                acertoGeral,
                perguntas.size(),
                totalAcertos,
                pontosTotais,
                usuarioAtualizado.getSaldo(),
                ofensiva.ofensivaAtual(),
                detalhes,
                feedback
        );
    }

    /**
     * Verifica se o usuário já realizou a tentativa diária em determinado quiz na data atual.
     */
    @Transactional(readOnly = true)
    public StatusQuizDiarioResponse verificarStatusDiario(Long idUsuario, Long idQuiz) {
        LocalDate hoje = LocalDate.now();
        Quiz quiz = buscarEntidadePorId(idQuiz);

        return quizTentativaDiariaRepository
                .findByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(idUsuario, idQuiz, hoje)
                .map(tentativa -> new StatusQuizDiarioResponse(
                        quiz.getIdQuiz(),
                        quiz.getTitulo(),
                        quiz.getDataDisponivel(),
                        true,
                        tentativa.getPontosGanhos(),
                        tentativa.getAcerto()
                ))
                .orElseGet(() -> new StatusQuizDiarioResponse(
                        quiz.getIdQuiz(),
                        quiz.getTitulo(),
                        quiz.getDataDisponivel(),
                        false,
                        0,
                        null
                ));
    }

    /**
     * Retorna o histórico de todas as tentativas de quiz realizadas pelo usuário.
     */
    @Transactional(readOnly = true)
    public List<QuizTentativaDiaria> listarTentativasUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + idUsuario);
        }
        return quizTentativaDiariaRepository.findByUsuarioIdUsuarioOrderByDataJogadaDesc(idUsuario);
    }

    /**
     * Exclui um Quiz e suas respectivas perguntas e alternativas em cascata.
     */
    @Transactional
    public void deletarQuiz(Long idQuiz) {
        Quiz quiz = buscarEntidadePorId(idQuiz);
        quizRepository.delete(quiz);
    }

    /**
     * Regra de pontuação ecológica baseada no nível de dificuldade.
     */
    private int calcularPontosPorDificuldade(NivelDificuldade nivel) {
        if (nivel == null) return 10;
        return switch (nivel) {
            case FACIL -> 10;
            case MEDIO -> 20;
            case DIFICIL -> 30;
        };
    }

    private String gerarMensagemFeedback(int acertos, int total, int pontos, int ofensiva) {
        if (acertos == total) {
            return String.format("Excelente! Você gabaritou o quiz ambiental, ganhou %d pontos e sua ofensiva agora é de %d dias consecutivos!", pontos, ofensiva);
        } else if (acertos > 0) {
            return String.format("Bom trabalho! Você acertou %d de %d perguntas, somou %d pontos e sua ofensiva subiu para %d dias!", acertos, total, pontos, ofensiva);
        } else {
            return String.format("Não desanime! Revise o conteúdo ecológico e volte amanhã. Sua presença manteve a ofensiva em %d dias!", ofensiva);
        }
    }
}
