package com.ambientiza.api.services;

import com.ambientiza.api.dtos.usuarios.*;
import com.ambientiza.api.exceptions.RecursoNaoEncontradoException;
import com.ambientiza.api.exceptions.RegraDeNegocioException;
import com.ambientiza.api.exceptions.SaldoInsuficienteException;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cadastra um novo usuário com saldo inicial zero e ofensiva zerada.
     */
    @Transactional
    public UsuarioResponse cadastrar(CadastroUsuario dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("O email informado já está em uso.");
        }

        if (usuarioRepository.existsByNome(dto.nome())) {
            throw new RegraDeNegocioException("O nome de usuário informado já está em uso.");
        }

        String senhaHash = gerarHash(dto.senha());

        Usuarios usuario = new Usuarios();
        usuario.setNome(dto.nome().trim());
        usuario.setEmail(dto.email().trim().toLowerCase());
        usuario.setSenhaHash(senhaHash);
        usuario.setSaldo(BigDecimal.ZERO);
        usuario.setOfensivaAtual(0);
        usuario.setCriadoEm(LocalDate.now());
        usuario.setUltimaAtividade(null);

        Usuarios usuarioSalvo = usuarioRepository.save(usuario);
        return new UsuarioResponse(usuarioSalvo);
    }

    /**
     * Realiza login validando nome de usuário ou email e a senha digitada.
     */
    @Transactional(readOnly = true)
    public UsuarioResponse login(LoginUsuario dto) {
        String identificador = dto.login().trim();

        Usuarios usuario = usuarioRepository.findByEmail(identificador.toLowerCase())
                .or(() -> usuarioRepository.findByNome(identificador))
                .orElseThrow(() -> new RegraDeNegocioException("Credenciais inválidas: usuário ou email não encontrado."));

        if (!verificarHashSenha(dto.senha(), usuario.getSenhaHash())) {
            throw new RegraDeNegocioException("Credenciais inválidas: senha incorreta.");
        }

        return new UsuarioResponse(usuario);
    }

    /**
     * Retorna a lista simplificada com os nomes dos usuários (utilizado pelo UsuarioController).
     */
    @Transactional(readOnly = true)
    public List<ListarNomes> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(ListarNomes::new)
                .toList();
    }

    /**
     * Retorna a lista completa com todos os dados públicos dos usuários.
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponse::new)
                .toList();
    }

    /**
     * Busca um usuário pelo ID e retorna o DTO seguro.
     */
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return new UsuarioResponse(buscarEntidadePorId(id));
    }

    /**
     * Busca um usuário pelo email.
     */
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email.trim().toLowerCase())
                .map(UsuarioResponse::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o email: " + email));
    }

    /**
     * Busca a entidade Usuarios diretamente (para consumo interno por outros services, como Quiz e Recompensas).
     */
    @Transactional(readOnly = true)
    public Usuarios buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id));
    }

    /**
     * Atualiza os dados cadastrais (nome e email) de um usuário existente.
     */
    @Transactional
    public UsuarioResponse atualizar(Long id, AtualizarUsuario dto) {
        Usuarios usuario = buscarEntidadePorId(id);

        String novoEmail = dto.email().trim().toLowerCase();
        if (!usuario.getEmail().equalsIgnoreCase(novoEmail)) {
            usuarioRepository.findByEmail(novoEmail).ifPresent(existente -> {
                if (!existente.getIdUsuario().equals(id)) {
                    throw new RegraDeNegocioException("O email informado já pertence a outro usuário.");
                }
            });
            usuario.setEmail(novoEmail);
        }

        String novoNome = dto.nome().trim();
        if (!usuario.getNome().equalsIgnoreCase(novoNome)) {
            usuarioRepository.findByNome(novoNome).ifPresent(existente -> {
                if (!existente.getIdUsuario().equals(id)) {
                    throw new RegraDeNegocioException("O nome informado já pertence a outro usuário.");
                }
            });
            usuario.setNome(novoNome);
        }

        Usuarios salvo = usuarioRepository.save(usuario);
        return new UsuarioResponse(salvo);
    }

    /**
     * Altera a senha do usuário após validação da senha atual.
     */
    @Transactional
    public void alterarSenha(Long id, AlterarSenha dto) {
        Usuarios usuario = buscarEntidadePorId(id);

        if (!verificarHashSenha(dto.senhaAtual(), usuario.getSenhaHash())) {
            throw new RegraDeNegocioException("A senha atual fornecida está incorreta.");
        }

        usuario.setSenhaHash(gerarHash(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }

    /**
     * Credita pontos no saldo do usuário (ex: conclusão de quizzes diários ou tarefas sustentáveis).
     */
    @Transactional
    public UsuarioResponse adicionarPontos(Long idUsuario, BigDecimal pontos) {
        if (pontos == null || pontos.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("A quantidade de pontos a adicionar deve ser positiva.");
        }

        Usuarios usuario = buscarEntidadePorId(idUsuario);
        BigDecimal saldoAtual = usuario.getSaldo() != null ? usuario.getSaldo() : BigDecimal.ZERO;
        usuario.setSaldo(saldoAtual.add(pontos));

        Usuarios salvo = usuarioRepository.save(usuario);
        return new UsuarioResponse(salvo);
    }

    /**
     * Debita pontos do saldo do usuário de forma transacional (ex: resgate de benefícios na loja).
     * Garante que o saldo nunca fique negativo e previne resgates indevidos.
     */
    @Transactional
    public UsuarioResponse debitarPontos(Long idUsuario, BigDecimal pontos) {
        if (pontos == null || pontos.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("A quantidade de pontos a debitar deve ser positiva.");
        }

        Usuarios usuario = buscarEntidadePorId(idUsuario);
        BigDecimal saldoAtual = usuario.getSaldo() != null ? usuario.getSaldo() : BigDecimal.ZERO;

        if (saldoAtual.compareTo(pontos) < 0) {
            throw new SaldoInsuficienteException(saldoAtual, pontos);
        }

        usuario.setSaldo(saldoAtual.subtract(pontos));
        Usuarios salvo = usuarioRepository.save(usuario);
        return new UsuarioResponse(salvo);
    }

    /**
     * Mecânica de Ofensiva (Streak) estilo Duolingo:
     * - Se primeira atividade: ofensiva = 1.
     * - Se jogou hoje novamente: mantém a ofensiva sem alterar.
     * - Se jogou no dia anterior consecutivo (ontem): incrementa ofensiva em +1.
     * - Se perdeu um ou mais dias: reinicia a ofensiva em 1.
     */
    @Transactional
    public OfensivaResponse registrarAtividadeEAtualizarOfensiva(Long idUsuario) {
        Usuarios usuario = buscarEntidadePorId(idUsuario);
        LocalDate hoje = LocalDate.now();
        LocalDate ultima = usuario.getUltimaAtividade();

        if (ultima == null) {
            usuario.setOfensivaAtual(1);
            usuario.setUltimaAtividade(hoje);
        } else if (ultima.equals(hoje)) {
            // Já realizou atividade hoje, ofensiva mantida
        } else if (ultima.equals(hoje.minusDays(1))) {
            // Dia consecutivo: incrementa
            int ofensiva = usuario.getOfensivaAtual() != null ? usuario.getOfensivaAtual() : 0;
            usuario.setOfensivaAtual(ofensiva + 1);
            usuario.setUltimaAtividade(hoje);
        } else {
            // Perdeu um ou mais dias: quebra da sequência e reinício
            usuario.setOfensivaAtual(1);
            usuario.setUltimaAtividade(hoje);
        }

        Usuarios salvo = usuarioRepository.save(usuario);
        return new OfensivaResponse(salvo);
    }

    /**
     * Consulta o status atual de ofensiva do usuário.
     */
    @Transactional(readOnly = true)
    public OfensivaResponse consultarOfensiva(Long idUsuario) {
        Usuarios usuario = buscarEntidadePorId(idUsuario);
        return new OfensivaResponse(usuario);
    }

    /**
     * Remove um usuário pelo ID.
     */
    @Transactional
    public void deletar(Long id) {
        Usuarios usuario = buscarEntidadePorId(id);
        usuarioRepository.delete(usuario);
    }

    /**
     * Algoritmo de hash SHA-256 para armazenamento seguro de senhas.
     */
    private String gerarHash(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new RegraDeNegocioException("A senha não pode estar em branco.");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao calcular hash de senha", e);
        }
    }

    /**
     * Valida se a senha digitada corresponde ao hash cadastrado.
     */
    private boolean verificarHashSenha(String senhaDigitada, String hashArmazenado) {
        if (senhaDigitada == null || hashArmazenado == null) {
            return false;
        }
        return gerarHash(senhaDigitada).equalsIgnoreCase(hashArmazenado);
    }
}
