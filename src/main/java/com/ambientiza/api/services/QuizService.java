package com.ambientiza.api.services;

import com.ambientiza.api.dtos.quiz.*;
import com.ambientiza.api.models.*;
import com.ambientiza.api.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizPerguntasRepository quizPerguntasRepository;
    private final QuizAlternativasRepository quizAlternativasRepository;
    private final QuizTentativaDiariaRepository tentativaRepository;
    private final UsuarioRepository usuarioRepository;

    public QuizService(QuizRepository quizRepository,
                       QuizPerguntasRepository quizPerguntasRepository,
                       QuizAlternativasRepository quizAlternativasRepository,
                       QuizTentativaDiariaRepository tentativaRepository,
                       UsuarioRepository usuarioRepository) {
        this.quizRepository = quizRepository;
        this.quizPerguntasRepository = quizPerguntasRepository;
        this.quizAlternativasRepository = quizAlternativasRepository;
        this.tentativaRepository = tentativaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Retorna o quiz do dia se o usuário ainda não tiver jogado.
     */
    @Transactional(readOnly = true)
    public QuizExibicaoResponseDTO obterQuizDoDia(Long idUsuario, LocalDate data) {
        // 1. Busca o Quiz programado para o dia corrente
        Quiz quiz = quizRepository.findByDataDisponivel(data)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum quiz disponível para a data de hoje."));

        // 2. Garante a regra de 1 tentativa diária
        boolean jaJogou = tentativaRepository.existsByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(
                idUsuario, quiz.getIdQuiz(), data);
        if (jaJogou) {
            throw new IllegalStateException("Você já realizou a sua tentativa do quiz de hoje!");
        }

        // 3. Mapeamento manual para o DTO sem o campo de "correta"
        List<QuizPerguntas> perguntas = quizPerguntasRepository.findByQuiz(quiz);

        List<PerguntaOutputDTO> perguntasDto = perguntas.stream().map(pergunta -> {
            List<QuizAlternativas> alternativas = quizAlternativasRepository.findByQuizPerguntas(pergunta);
            List<AlternativaOutputDTO> alternativasDto = alternativas.stream()
                    .map(alt -> new AlternativaOutputDTO(alt.getIdQuizAlternativas(), alt.getTexto()))
                    .toList();

            return new PerguntaOutputDTO(
                    pergunta.getIdPergunta(),
                    pergunta.getEnunciado(),
                    pergunta.getNivelDificuldade(),
                    alternativasDto
            );
        }).toList();

        return new QuizExibicaoResponseDTO(
                quiz.getIdQuiz(),
                quiz.getTitulo(),
                quiz.getTema(),
                quiz.getDataDisponivel(),
                perguntasDto
        );
    }

    /**
     * Valida, corrige e pontua a submissão do Quiz de forma segura.
     */
    @Transactional
    public QuizTentativaResponseDTO enviarResposta(QuizSubmissaoRequestDTO submissao) {
        LocalDate hoje = LocalDate.now();

        // 1. Garante que o jogador existe
        Usuarios usuario = usuarioRepository.findById(submissao.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        // 2. Garante que o quiz existe
        Quiz quiz = quizRepository.findById(submissao.idQuiz())
                .orElseThrow(() -> new IllegalArgumentException("Quiz não encontrado."));

        // 3. Regra de Negócio: Bloqueia tentativas extras
        boolean jaJogou = tentativaRepository.existsByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(
                usuario.getIdUsuario(), quiz.getIdQuiz(), hoje);
        if (jaJogou) {
            throw new IllegalStateException("Tentativa diária bloqueada: Você já participou deste Quiz hoje.");
        }

        // 4. Correção da resposta e verificação das alternativas
        QuizAlternativas alternativaEscolhida = quizAlternativasRepository.findById(submissao.idAlternativaEscolhida())
                .orElseThrow(() -> new IllegalArgumentException("Alternativa não cadastrada para este quiz."));

        // Garante que a alternativa pertence a uma pergunta deste quiz
        if (alternativaEscolhida.getQuizPerguntas() == null ||
                alternativaEscolhida.getQuizPerguntas().getQuiz() == null ||
                !alternativaEscolhida.getQuizPerguntas().getQuiz().getIdQuiz().equals(quiz.getIdQuiz())) {
            throw new IllegalArgumentException("Alternativa não pertence a este quiz.");
        }

        boolean acerto = Boolean.TRUE.equals(alternativaEscolhida.getCorreta());
        int pontosGanhos = acerto ? 50 : 0; // Configuração padrão de pontos do game

        // 5. Atualiza o saldo e a ofensiva do Usuário
        if (acerto) {
            BigDecimal saldoAtual = usuario.getSaldo() != null ? usuario.getSaldo() : BigDecimal.ZERO;
            usuario.setSaldo(saldoAtual.add(BigDecimal.valueOf(pontosGanhos)));
        }

        // Regra da Ofensiva: lê a ofensiva atual do usuário e adiciona +1
        int ofensivaAtual = usuario.getOfensivaAtual() != null ? usuario.getOfensivaAtual() : 0;
        usuario.setOfensivaAtual(ofensivaAtual + 1);
        usuarioRepository.save(usuario);

        // 6. Grava a tentativa no BD para impossibilitar duplicatas
        QuizTentativaDiaria tentativa = new QuizTentativaDiaria();
        tentativa.setQuiz(quiz);
        tentativa.setUsuario(usuario);
        tentativa.setDataJogada(hoje);
        tentativa.setAcerto(acerto ? 1 : 0);
        tentativa.setPontosGanhos(pontosGanhos);
        tentativa = tentativaRepository.save(tentativa);

        return new QuizTentativaResponseDTO(
                tentativa.getIdQuizTentativaDiaria(),
                tentativa.getDataJogada(),
                acerto,
                tentativa.getPontosGanhos(),
                usuario.getSaldo(),
                usuario.getOfensivaAtual()
        );
    }

    /**
     * Cria um novo quiz completo com suas perguntas e alternativas.
     */
    @Transactional
    public QuizExibicaoResponseDTO criarQuiz(QuizRequestDTO dto) {
        Quiz quiz = new Quiz();
        quiz.setTitulo(dto.titulo());
        quiz.setTema(dto.tema());
        quiz.setDataDisponivel(dto.dataDisponivel());
        Quiz quizSalvo = quizRepository.save(quiz);

        List<PerguntaOutputDTO> perguntasDto = dto.perguntas().stream().map(perguntaDto -> {
            QuizPerguntas pergunta = new QuizPerguntas();
            pergunta.setQuiz(quizSalvo);
            pergunta.setEnunciado(perguntaDto.enunciado());
            pergunta.setNivelDificuldade(perguntaDto.nivelDificuldade());
            QuizPerguntas perguntaSalva = quizPerguntasRepository.save(pergunta);

            List<AlternativaOutputDTO> alternativasDto = perguntaDto.alternativas().stream().map(altDto -> {
                QuizAlternativas alt = new QuizAlternativas();
                alt.setQuizPerguntas(perguntaSalva);
                alt.setTexto(altDto.texto());
                alt.setCorreta(altDto.correta());
                QuizAlternativas altSalva = quizAlternativasRepository.save(alt);
                return new AlternativaOutputDTO(altSalva.getIdQuizAlternativas(), altSalva.getTexto());
            }).toList();

            return new PerguntaOutputDTO(
                    perguntaSalva.getIdPergunta(),
                    perguntaSalva.getEnunciado(),
                    perguntaSalva.getNivelDificuldade(),
                    alternativasDto
            );
        }).toList();

        return new QuizExibicaoResponseDTO(
                quizSalvo.getIdQuiz(),
                quizSalvo.getTitulo(),
                quizSalvo.getTema(),
                quizSalvo.getDataDisponivel(),
                perguntasDto
        );
    }
}