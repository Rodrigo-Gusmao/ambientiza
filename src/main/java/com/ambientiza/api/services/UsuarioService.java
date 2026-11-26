package com.ambientiza.api.services;

import com.ambientiza.api.dtos.Respostas.EstatisticasResponse;
import com.ambientiza.api.dtos.Respostas.LoginResponse;
import com.ambientiza.api.dtos.Respostas.ResgateResponse;
import com.ambientiza.api.dtos.Respostas.UsuarioResponse;
import com.ambientiza.api.dtos.usuarios.Cadastro;
import com.ambientiza.api.dtos.usuarios.LoginUsuario;
import com.ambientiza.api.enums.Papel;
import com.ambientiza.api.exceptions.ApiException;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.ProgressoTarefasRepository;
import com.ambientiza.api.repositories.QuizTentativaDiariaRepository;
import com.ambientiza.api.repositories.ResgateRecompensaRepository;
import com.ambientiza.api.repositories.UsuarioRepository;
import com.ambientiza.api.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ProgressoTarefasRepository progressoRepository;
    private final QuizTentativaDiariaRepository tentativaRepository;
    private final ResgateRecompensaRepository resgateRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UsuarioService(UsuarioRepository usuarioRepository, ProgressoTarefasRepository progressoRepository,
                          QuizTentativaDiariaRepository tentativaRepository, ResgateRecompensaRepository resgateRepository,
                          PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.progressoRepository = progressoRepository;
        this.tentativaRepository = tentativaRepository;
        this.resgateRepository = resgateRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public LoginResponse cadastrar(Cadastro cadastro) {
        String email = normalizarEmail(cadastro.email());
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw ApiException.conflito("Esse e-mail já está cadastrado.");
        }
        Usuarios u = criar(cadastro.nome().trim(), email, cadastro.senha(), Papel.USUARIO);
        return new LoginResponse(tokenService.gerar(u.getIdUsuario()), paraResponse(u));
    }

    public Usuarios criar(String nome, String email, String senha, Papel papel) {
        Usuarios u = new Usuarios();
        u.setNome(nome);
        u.setEmail(normalizarEmail(email));
        u.setSenhaHash(passwordEncoder.encode(senha));
        u.setPapel(papel);
        u.setSaldo(0);
        u.setOfensivaAtual(0);
        u.setCriadoEm(LocalDate.now());
        return usuarioRepository.save(u);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginUsuario login) {
        Usuarios u = usuarioRepository.findByEmailIgnoreCase(normalizarEmail(login.email()))
                .filter(x -> passwordEncoder.matches(login.senha(), x.getSenhaHash()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos."));
        return new LoginResponse(tokenService.gerar(u.getIdUsuario()), paraResponse(u));
    }

    public void logout(String token) {
        tokenService.revogar(token);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(Long idUsuario) {
        return paraResponse(obter(idUsuario));
    }

    @Transactional(readOnly = true)
    public EstatisticasResponse estatisticas(Long idUsuario) {
        return new EstatisticasResponse(
                progressoRepository.countByUsuarioIdUsuario(idUsuario),
                tentativaRepository.countByUsuarioIdUsuario(idUsuario));
    }

    @Transactional(readOnly = true)
    public List<ResgateResponse> resgates(Long idUsuario) {
        return resgateRepository.findByUsuarioIdUsuarioOrderByDataResgateDescIdResgateDesc(idUsuario).stream()
                .map(ResgateResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::paraResponse).toList();
    }

    @Transactional
    public UsuarioResponse alterarPapel(Long idAlvo, Papel papel, Long idAdmin) {
        if (idAlvo.equals(idAdmin) && papel != Papel.ADMIN) {
            throw ApiException.invalido("Você não pode remover seu próprio acesso de administrador.");
        }
        Usuarios u = obter(idAlvo);
        u.setPapel(papel);
        return paraResponse(u);
    }

    public Usuarios obter(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> ApiException.naoEncontrado("Usuário não encontrado."));
    }

    public Usuarios obterParaAtualizar(Long idUsuario) {
        return usuarioRepository.findByIdParaAtualizar(idUsuario)
                .orElseThrow(() -> ApiException.naoEncontrado("Usuário não encontrado."));
    }

    public void registrarAtividade(Usuarios u) {
        LocalDate hoje = LocalDate.now();
        if (hoje.equals(u.getUltimaAtividade())) return;
        boolean consecutivo = hoje.minusDays(1).equals(u.getUltimaAtividade());
        u.setOfensivaAtual(consecutivo ? u.getOfensivaAtual() + 1 : 1);
        u.setUltimaAtividade(hoje);
    }

    public UsuarioResponse paraResponse(Usuarios u) {
        LocalDate hoje = LocalDate.now();
        LocalDate ultima = u.getUltimaAtividade();
        boolean vigente = ultima != null && !ultima.isBefore(hoje.minusDays(1));
        return UsuarioResponse.de(u, vigente ? u.getOfensivaAtual() : 0);
    }

    private static String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
