package com.ambientiza.api.dtos;

import com.ambientiza.api.enums.NivelDificuldade;
import com.ambientiza.api.enums.Papel;
import com.ambientiza.api.enums.Temas;
import com.ambientiza.api.enums.Tipo;
import com.ambientiza.api.models.*;

import java.time.LocalDate;
import java.util.List;

// Respostas da API: entidades nunca são serializadas direto (evita expor senhaHash e alternativas corretas).
public final class Respostas {
    private Respostas() {}

    public record UsuarioResponse(Long idUsuario, String nome, String email, Integer saldo, Integer ofensivaAtual,
                                  LocalDate ultimaAtividade, LocalDate criadoEm, Papel papel) {
        public static UsuarioResponse de(Usuarios u, int ofensivaVigente) {
            return new UsuarioResponse(u.getIdUsuario(), u.getNome(), u.getEmail(), u.getSaldo(), ofensivaVigente,
                    u.getUltimaAtividade(), u.getCriadoEm(), u.getPapel());
        }
    }

    public record LoginResponse(String token, UsuarioResponse usuario) {}

    public record EstatisticasResponse(long tarefasFeitas, long quizzesFeitos) {}

    public record TarefaResponse(Long idTarefa, String titulo, String descricao, Integer pontosRecompensa, Tipo tipo,
                                 boolean concluida) {
        public static TarefaResponse de(Tarefas t, boolean concluida) {
            return new TarefaResponse(t.getIdTarefa(), t.getTitulo(), t.getDescricao(), t.getPontosRecompensa(),
                    t.getTipo(), concluida);
        }
    }

    public record RecompensaResponse(Long idRecompensa, String nome, String descricao, Integer custo, Integer estoque) {
        public static RecompensaResponse de(Recompensas r) {
            return new RecompensaResponse(r.getIdRecompensa(), r.getNome(), r.getDescricao(), r.getCusto(), r.getEstoque());
        }
    }

    public record ResgateResponse(Long idResgate, Long idRecompensa, String nomeRecompensa, Integer pontosGastos,
                                  LocalDate dataResgate) {
        public static ResgateResponse de(ResgateRecompensas r) {
            return new ResgateResponse(r.getIdResgate(), r.getRecompensa().getIdRecompensa(),
                    r.getRecompensa().getNome(), r.getPontosGastos(), r.getDataResgate());
        }
    }

    public record QuizResponse(Long idQuiz, String titulo, Temas tema, LocalDate dataDisponivel, int totalPerguntas) {
        public static QuizResponse de(Quiz q) {
            return new QuizResponse(q.getIdQuiz(), q.getTitulo(), q.getTema(), q.getDataDisponivel(), q.getPerguntas().size());
        }
    }

    public record AlternativaResponse(Long idQuizAlternativas, String texto) {}

    public record PerguntaResponse(Long idPergunta, String enunciado, NivelDificuldade nivelDificuldade,
                                   Integer pontos, List<AlternativaResponse> alternativas) {
        public static PerguntaResponse de(QuizPerguntas p) {
            return new PerguntaResponse(p.getIdPergunta(), p.getEnunciado(), p.getNivelDificuldade(),
                    p.getNivelDificuldade().getPontos(),
                    p.getAlternativas().stream()
                            .map(a -> new AlternativaResponse(a.getIdQuizAlternativas(), a.getTexto()))
                            .toList());
        }
    }

    public record TentativaResponse(Long idQuizTentativaDiaria, Long idQuiz, LocalDate dataJogada, Integer acerto,
                                    Integer pontosGanhos) {
        public static TentativaResponse de(QuizTentativaDiaria t) {
            return new TentativaResponse(t.getIdQuizTentativaDiaria(), t.getQuiz().getIdQuiz(), t.getDataJogada(),
                    t.getAcerto(), t.getPontosGanhos());
        }
    }

    // valeMetade: o quiz é de um dia anterior (até 7 dias atrás), então rende só metade dos pontos.
    public record QuizDoDiaResponse(QuizResponse quiz, List<PerguntaResponse> perguntas, List<Long> respondidas,
                                    TentativaResponse tentativa, boolean valeMetade) {}

    public record QuizDisponivelResponse(QuizResponse quiz, int respondidas, TentativaResponse tentativa,
                                         boolean valeMetade) {}

    public record ResultadoRespostaResponse(boolean correta, Long idAlternativaCorreta) {}

    public record FinalizarQuizResponse(TentativaResponse tentativa, UsuarioResponse usuario) {}

    public record ErroResponse(String message) {}
}
