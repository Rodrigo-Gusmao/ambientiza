package com.ambientiza.api.config;

import com.ambientiza.api.dtos.Requisicoes.*;
import com.ambientiza.api.enums.NivelDificuldade;
import com.ambientiza.api.enums.Papel;
import com.ambientiza.api.enums.Temas;
import com.ambientiza.api.enums.Tipo;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.TarefasRepository;
import com.ambientiza.api.repositories.UsuarioRepository;
import com.ambientiza.api.services.QuizService;
import com.ambientiza.api.services.RecompensaService;
import com.ambientiza.api.services.TarefaService;
import com.ambientiza.api.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class DadosIniciais implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DadosIniciais.class);

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final TarefasRepository tarefasRepository;
    private final TarefaService tarefaService;
    private final RecompensaService recompensaService;
    private final QuizService quizService;

    @Value("${ambientiza.admin.email}")
    private String adminEmail;
    @Value("${ambientiza.admin.senha}")
    private String adminSenha;
    @Value("${ambientiza.seed.demo:false}")
    private boolean seedDemo;

    public DadosIniciais(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
                         TarefasRepository tarefasRepository, TarefaService tarefaService,
                         RecompensaService recompensaService, QuizService quizService) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.tarefasRepository = tarefasRepository;
        this.tarefaService = tarefaService;
        this.recompensaService = recompensaService;
        this.quizService = quizService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!usuarioRepository.existsByEmailIgnoreCase(adminEmail)) {
            usuarioService.criar("Administrador", adminEmail, adminSenha, Papel.ADMIN);
            log.info("Usuário administrador criado: {}", adminEmail);
        }
        if (seedDemo && tarefasRepository.count() == 0) {
            criarDadosDemo();
            log.info("Dados de demonstração criados (usuário arthur@ambientiza.com).");
        }
    }

    private void criarDadosDemo() {
        tarefaService.salvar(null, new TarefaRequest("Separar o lixo reciclável", "Separe plástico, papel, metal e vidro.", 20, Tipo.DIARIO));
        tarefaService.salvar(null, new TarefaRequest("Banho de 5 minutos", "Economize água com um banho rápido.", 15, Tipo.DIARIO));
        tarefaService.salvar(null, new TarefaRequest("Usar sacola reutilizável", "Leve sua sacola ao fazer compras.", 10, Tipo.DIARIO));

        recompensaService.salvar(null, new RecompensaRequest("Muda de árvore nativa", "Muda de espécie nativa da região.", 300, 14));
        recompensaService.salvar(null, new RecompensaRequest("Copo reutilizável", "Copo de fibra de bambu.", 600, 8));
        recompensaService.salvar(null, new RecompensaRequest("Ecobag de algodão", "Sacola de algodão cru.", 450, 0));

        quizService.criar(new QuizRequest("Quiz do dia", Temas.GESTAO_RECICLAGEM, LocalDate.now(), List.of(
                pergunta("Qual a cor da lixeira de coleta seletiva para plástico?", NivelDificuldade.FACIL, 0,
                        "Vermelha", "Azul", "Amarela", "Verde"),
                pergunta("O que pode ir para a composteira doméstica?", NivelDificuldade.FACIL, 0,
                        "Cascas de frutas", "Pilhas", "Isopor", "Embalagens plásticas"),
                pergunta("Qual material leva mais tempo para se decompor?", NivelDificuldade.MEDIO, 2,
                        "Casca de banana", "Papel", "Vidro", "Lata de alumínio"),
                pergunta("Qual atitude reduz o consumo de energia em casa?", NivelDificuldade.MEDIO, 1,
                        "Deixar aparelhos em stand-by", "Trocar lâmpadas por LED", "Abrir a geladeira com frequência", "Usar a secadora todos os dias"),
                pergunta("Por que as abelhas são importantes para a biodiversidade?", NivelDificuldade.DIFICIL, 3,
                        "Produzem oxigênio", "Controlam pragas do solo", "Decompõem matéria orgânica", "Polinizam as plantas")
        )));

        quizService.criar(new QuizRequest("Água e energia", Temas.GESTAO_RECICLAGEM, LocalDate.now().minusDays(2), List.of(
                pergunta("Qual atitude economiza mais água no dia a dia?", NivelDificuldade.FACIL, 1,
                        "Lavar a calçada com mangueira", "Fechar a torneira ao escovar os dentes", "Tomar banhos longos", "Descongelar alimentos em água corrente"),
                pergunta("Qual fonte de energia é renovável?", NivelDificuldade.FACIL, 2,
                        "Carvão mineral", "Petróleo", "Solar", "Gás natural"),
                pergunta("O que é o \"modo stand-by\" de um aparelho?", NivelDificuldade.MEDIO, 0,
                        "Aparelho desligado, mas ainda consumindo energia", "Aparelho fora da tomada", "Modo de economia total", "Aparelho em manutenção")
        )));

        Usuarios arthur = usuarioService.criar("Arthur Rodrigues", "arthur@ambientiza.com", "123456", Papel.USUARIO);
        arthur.setSaldo(1240);
        arthur.setOfensivaAtual(11);
        arthur.setUltimaAtividade(LocalDate.now().minusDays(1));
        arthur.setCriadoEm(LocalDate.now().minusMonths(2));
    }

    private static PerguntaRequest pergunta(String enunciado, NivelDificuldade nivel, int indiceCorreta, String... textos) {
        var alternativas = new java.util.ArrayList<AlternativaRequest>();
        for (int i = 0; i < textos.length; i++) {
            alternativas.add(new AlternativaRequest(textos[i], i == indiceCorreta));
        }
        return new PerguntaRequest(enunciado, nivel, alternativas);
    }
}
