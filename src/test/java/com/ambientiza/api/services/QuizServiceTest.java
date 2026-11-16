package com.ambientiza.api.services;

import com.ambientiza.api.dtos.quiz.*;
import com.ambientiza.api.dtos.usuarios.OfensivaResponse;
import com.ambientiza.api.dtos.usuarios.UsuarioResponse;
import com.ambientiza.api.enums.NivelDificuldade;
import com.ambientiza.api.enums.Temas;
import com.ambientiza.api.exceptions.RegraDeNegocioException;
import com.ambientiza.api.exceptions.TentativaJaRealizadaException;
import com.ambientiza.api.models.Quiz;
import com.ambientiza.api.models.QuizAlternativas;
import com.ambientiza.api.models.QuizPerguntas;
import com.ambientiza.api.models.QuizTentativaDiaria;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizPerguntasRepository quizPerguntasRepository;

    @Mock
    private QuizAlternativasRepository quizAlternativasRepository;

    @Mock
    private QuizTentativaDiariaRepository quizTentativaDiariaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private QuizService quizService;

    private Usuarios usuario;
    private Quiz quiz;
    private QuizPerguntas pergunta;
    private QuizAlternativas alternativaCorreta;
    private QuizAlternativas alternativaIncorreta;

    @BeforeEach
    void setUp() {
        usuario = new Usuarios();
        usuario.setIdUsuario(1L);
        usuario.setNome("Rodrigo Silva");
        usuario.setEmail("rodrigo@email.com");
        usuario.setSaldo(BigDecimal.ZERO);
        usuario.setOfensivaAtual(2);

        quiz = new Quiz("Descarte Consciente de Plásticos", Temas.GESTAO_RECICLAGEM, LocalDate.now());
        quiz.setIdQuiz(10L);

        pergunta = new QuizPerguntas(quiz, "Qual a cor da lixeira para descarte de plástico?", NivelDificuldade.FACIL);
        pergunta.setIdPergunta(100L);

        alternativaCorreta = new QuizAlternativas(pergunta, "Vermelho", true);
        alternativaCorreta.setIdAlternativa(1001L);

        alternativaIncorreta = new QuizAlternativas(pergunta, "Azul", false);
        alternativaIncorreta.setIdAlternativa(1002L);

        pergunta.adicionarAlternativa(alternativaCorreta);
        pergunta.adicionarAlternativa(alternativaIncorreta);
        quiz.adicionarPergunta(pergunta);
    }

    @Test
    @DisplayName("Deve cadastrar um Quiz completo com perguntas e alternativas com sucesso")
    void cadastrarQuizSucesso() {
        CadastroAlternativa a1 = new CadastroAlternativa("Compostagem", true);
        CadastroAlternativa a2 = new CadastroAlternativa("Incineração comum", false);

        CadastroPergunta p1 = new CadastroPergunta(
                "O que fazer com restos de cascas de frutas?",
                NivelDificuldade.MEDIO,
                List.of(a1, a2)
        );

        CadastroQuiz dto = new CadastroQuiz(
                "Manejo de Resíduos Orgânicos",
                Temas.GESTAO_RECICLAGEM,
                LocalDate.now(),
                List.of(p1)
        );

        when(quizRepository.save(any(Quiz.class))).thenAnswer(invocation -> {
            Quiz q = invocation.getArgument(0);
            q.setIdQuiz(50L);
            return q;
        });

        QuizResponse response = quizService.cadastrarQuiz(dto);

        assertNotNull(response);
        assertEquals(50L, response.idQuiz());
        assertEquals("Manejo de Resíduos Orgânicos", response.titulo());
        assertEquals(Temas.GESTAO_RECICLAGEM, response.tema());
        assertEquals(1, response.perguntas().size());
        verify(quizRepository, times(1)).save(any(Quiz.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se uma pergunta não tiver exatamente uma alternativa correta")
    void cadastrarQuizValidacaoAlternativaCorreta() {
        CadastroAlternativa a1 = new CadastroAlternativa("Opção 1", false);
        CadastroAlternativa a2 = new CadastroAlternativa("Opção 2", false); // Nenhuma correta

        CadastroPergunta p1 = new CadastroPergunta("Pergunta teste?", NivelDificuldade.FACIL, List.of(a1, a2));
        CadastroQuiz dto = new CadastroQuiz("Quiz Inválido", Temas.CONSUMO_CONSCIENTE, LocalDate.now(), List.of(p1));

        assertThrows(RegraDeNegocioException.class, () -> quizService.cadastrarQuiz(dto));
        verify(quizRepository, never()).save(any(Quiz.class));
    }

    @Test
    @DisplayName("Deve responder ao quiz diário com sucesso, somando pontos e atualizando ofensiva")
    void responderQuizSucesso() {
        ResponderQuizRequest request = new ResponderQuizRequest(
                1L,
                10L,
                List.of(new RespostaItem(100L, 1001L))
        );

        LocalDate hoje = LocalDate.now();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(quizRepository.findById(10L)).thenReturn(Optional.of(quiz));
        when(quizTentativaDiariaRepository.existsByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(1L, 10L, hoje))
                .thenReturn(false);

        when(quizAlternativasRepository.findById(1001L)).thenReturn(Optional.of(alternativaCorreta));
        when(quizAlternativasRepository.findByQuizPerguntasIdPerguntaAndCorretaTrue(100L))
                .thenReturn(Optional.of(alternativaCorreta));

        when(usuarioService.registrarAtividadeEAtualizarOfensiva(1L))
                .thenReturn(new OfensivaResponse(1L, "Rodrigo Silva", 3, hoje, true));

        when(quizTentativaDiariaRepository.save(any(QuizTentativaDiaria.class))).thenAnswer(invocation -> {
            QuizTentativaDiaria t = invocation.getArgument(0);
            t.setIdTentativa(999L);
            return t;
        });

        TentativaQuizResponse response = quizService.responderQuiz(request);

        assertNotNull(response);
        assertEquals(999L, response.idTentativa());
        assertTrue(response.acertoGeral());
        assertEquals(1, response.totalAcertos());
        assertEquals(10, response.pontosGanhos()); // Dificuldade FACIL = 10 pts
        assertEquals(3, response.ofensivaAtualizada());

        verify(usuarioService, times(1)).adicionarPontos(1L, BigDecimal.valueOf(10));
        verify(usuarioService, times(1)).registrarAtividadeEAtualizarOfensiva(1L);
        verify(quizTentativaDiariaRepository, times(1)).save(any(QuizTentativaDiaria.class));
    }

    @Test
    @DisplayName("Deve bloquear segunda tentativa no mesmo dia (Restrição de 1 tentativa diária)")
    void bloquearSegundaTentativaNoMesmoDia() {
        ResponderQuizRequest request = new ResponderQuizRequest(
                1L,
                10L,
                List.of(new RespostaItem(100L, 1001L))
        );

        LocalDate hoje = LocalDate.now();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(quizRepository.findById(10L)).thenReturn(Optional.of(quiz));
        when(quizTentativaDiariaRepository.existsByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(1L, 10L, hoje))
                .thenReturn(true); // Já jogou hoje!

        assertThrows(TentativaJaRealizadaException.class, () -> quizService.responderQuiz(request));

        verify(usuarioService, never()).adicionarPontos(any(), any());
        verify(usuarioService, never()).registrarAtividadeEAtualizarOfensiva(any());
        verify(quizTentativaDiariaRepository, never()).save(any(QuizTentativaDiaria.class));
    }

    @Test
    @DisplayName("Deve verificar se usuário já realizou a tentativa do quiz hoje")
    void verificarStatusDiario() {
        LocalDate hoje = LocalDate.now();
        QuizTentativaDiaria tentativa = new QuizTentativaDiaria(quiz, usuario, true, 20);

        when(quizRepository.findById(10L)).thenReturn(Optional.of(quiz));
        when(quizTentativaDiariaRepository.findByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(1L, 10L, hoje))
                .thenReturn(Optional.of(tentativa));

        StatusQuizDiarioResponse status = quizService.verificarStatusDiario(1L, 10L);

        assertTrue(status.jaRespondidoHoje());
        assertEquals(20, status.pontosGanhos());
        assertTrue(status.acerto());
    }
}
