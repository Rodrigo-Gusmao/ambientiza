package com.ambientiza.api.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambientiza.api.dtos.recompensas.AtualizarRecompensa;
import com.ambientiza.api.dtos.recompensas.CadastroRecompensa;
import com.ambientiza.api.dtos.recompensas.RecompensaResponse;
import com.ambientiza.api.dtos.recompensas.ResgateRequest;
import com.ambientiza.api.dtos.recompensas.ResgateResponse;
import com.ambientiza.api.exceptions.EstoqueInsuficienteException;
import com.ambientiza.api.exceptions.RecursoNaoEncontradoException;
import com.ambientiza.api.exceptions.RegraDeNegocioException;
import com.ambientiza.api.exceptions.SaldoInsuficienteException;
import com.ambientiza.api.models.Recompensas;
import com.ambientiza.api.models.ResgateRecompensas;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.RecompensasRepository;
import com.ambientiza.api.repositories.ResgateRecompensaRepository;
import com.ambientiza.api.repositories.UsuarioRepository;

@Service
public class RecompensaService {

    private final RecompensasRepository recompensasRepository;
    private final ResgateRecompensaRepository resgateRecompensaRepository;
    private final UsuarioRepository usuarioRepository;

    public RecompensaService(
            RecompensasRepository recompensasRepository,
            ResgateRecompensaRepository resgateRecompensaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.recompensasRepository = recompensasRepository;
        this.resgateRecompensaRepository = resgateRecompensaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cadastra um novo benefício ou recompensa no catálogo da loja ecológica.
     */
    @Transactional
    public RecompensaResponse cadastrar(CadastroRecompensa dto) {
        if (recompensasRepository.existsByNome(dto.nome().trim())) {
            throw new RegraDeNegocioException("Já existe uma recompensa cadastrada com o nome: " + dto.nome());
        }

        Recompensas recompensa = new Recompensas();
        recompensa.setNome(dto.nome().trim());
        recompensa.setDescricao(dto.descricao().trim());
        recompensa.setCustoPontos(dto.custoPontos());
        recompensa.setEstoque(dto.estoque());

        Recompensas salva = recompensasRepository.save(recompensa);
        return new RecompensaResponse(salva);
    }

    /**
     * Retorna todas as recompensas cadastradas.
     */
    @Transactional(readOnly = true)
    public List<RecompensaResponse> listarTodas() {
        return recompensasRepository.findAll()
                .stream()
                .map(RecompensaResponse::new)
                .toList();
    }

    /**
     * Retorna apenas as recompensas disponíveis para resgate (estoque > 0).
     */
    @Transactional(readOnly = true)
    public List<RecompensaResponse> listarDisponiveis() {
        return recompensasRepository.findByEstoqueGreaterThan(0)
                .stream()
                .map(RecompensaResponse::new)
                .toList();
    }

    /**
     * Busca uma recompensa pelo ID.
     */
    @Transactional(readOnly = true)
    public RecompensaResponse buscarPorId(Long id) {
        return new RecompensaResponse(buscarEntidadePorId(id));
    }

    /**
     * Busca a entidade Recompensas para operações internas.
     */
    @Transactional(readOnly = true)
    public Recompensas buscarEntidadePorId(Long id) {
        return recompensasRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Recompensa não encontrada com o ID: " + id));
    }

    /**
     * Atualiza dados de uma recompensa existente.
     */
    @Transactional
    public RecompensaResponse atualizar(Long id, AtualizarRecompensa dto) {
        Recompensas recompensa = buscarEntidadePorId(id);

        String novoNome = dto.nome().trim();
        if (!recompensa.getNome().equalsIgnoreCase(novoNome)) {
            recompensasRepository.findByNome(novoNome).ifPresent(existente -> {
                if (!existente.getIdRecompensa().equals(id)) {
                    throw new RegraDeNegocioException("Já existe outra recompensa com o nome: " + novoNome);
                }
            });
            recompensa.setNome(novoNome);
        }

        recompensa.setDescricao(dto.descricao().trim());
        recompensa.setCustoPontos(dto.custoPontos());
        recompensa.setEstoque(dto.estoque());

        Recompensas salva = recompensasRepository.save(recompensa);
        return new RecompensaResponse(salva);
    }

    /**
     * Adiciona itens ao estoque de uma recompensa.
     */
    @Transactional
    public RecompensaResponse adicionarEstoque(Long id, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new RegraDeNegocioException("A quantidade a adicionar ao estoque deve ser maior que zero.");
        }

        Recompensas recompensa = buscarEntidadePorId(id);
        recompensa.adicionarEstoque(quantidade);

        Recompensas salva = recompensasRepository.save(recompensa);
        return new RecompensaResponse(salva);
    }

    /**
     * Realiza o resgate de uma recompensa por um usuário com rigoroso controle transacional.
     * Regras garantidas:
     * 1. Verifica existência de usuário e recompensa.
     * 2. Valida disponibilidade de estoque (impede resgate sem itens).
     * 3. Valida saldo suficiente (impede saldo negativo).
     * 4. Deduz pontos do usuário e decrementa 1 unidade do estoque.
     * 5. Registra o pedido de resgate com timestamp para auditoria.
     */
    @Transactional
    public ResgateResponse resgatar(ResgateRequest dto) {
        Usuarios usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + dto.idUsuario()));

        Recompensas recompensa = buscarEntidadePorId(dto.idRecompensa());

        // 1. Verificação de estoque disponível
        if (recompensa.getEstoque() == null || recompensa.getEstoque() <= 0) {
            throw new EstoqueInsuficienteException(recompensa.getNome());
        }

        // 2. Verificação de saldo de pontos
        BigDecimal saldoAtual = usuario.getSaldo() != null ? usuario.getSaldo() : BigDecimal.ZERO;
        BigDecimal custo = recompensa.getCustoPontos();

        if (saldoAtual.compareTo(custo) < 0) {
            throw new SaldoInsuficienteException(saldoAtual, custo);
        }

        // 3. Atualização transacional: débito de pontos e baixa no estoque
        BigDecimal novoSaldo = saldoAtual.subtract(custo);
        usuario.setSaldo(novoSaldo);
        recompensa.decrementarEstoque();

        usuarioRepository.save(usuario);
        recompensasRepository.save(recompensa);

        // 4. Criação do registro de resgate
        ResgateRecompensas resgate = new ResgateRecompensas();
        resgate.setUsuario(usuario);
        resgate.setRecompensa(recompensa);
        resgate.setPontosGastos(custo);
        resgate.setDataResgate(LocalDateTime.now());

        ResgateRecompensas resgateSalvo = resgateRecompensaRepository.save(resgate);

        return new ResgateResponse(resgateSalvo, novoSaldo);
    }

    /**
     * Retorna o histórico cronológico de resgates de um usuário específico.
     */
    @Transactional(readOnly = true)
    public List<ResgateResponse> listarHistoricoUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + idUsuario);
        }

        return resgateRecompensaRepository.findByUsuarioIdUsuarioOrderByDataResgateDesc(idUsuario)
                .stream()
                .map(ResgateResponse::new)
                .toList();
    }

    /**
     * Retorna todos os resgates já efetuados na plataforma (para auditoria e relatórios).
     */
    @Transactional(readOnly = true)
    public List<ResgateResponse> listarTodosResgates() {
        return resgateRecompensaRepository.findAll()
                .stream()
                .map(ResgateResponse::new)
                .toList();
    }

    /**
     * Busca os detalhes de um resgate específico pelo ID.
     */
    @Transactional(readOnly = true)
    public ResgateResponse buscarResgatePorId(Long idResgate) {
        ResgateRecompensas resgate = resgateRecompensaRepository.findById(idResgate)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Resgate não encontrado com o ID: " + idResgate));
        return new ResgateResponse(resgate);
    }

    /**
     * Exclui uma recompensa do catálogo caso ainda não tenha sido resgatada.
     */
    @Transactional
    public void deletar(Long id) {
        Recompensas recompensa = buscarEntidadePorId(id);

        List<ResgateRecompensas> resgatesAssociados = resgateRecompensaRepository.findByRecompensaIdRecompensaOrderByDataResgateDesc(id);
        if (!resgatesAssociados.isEmpty()) {
            throw new RegraDeNegocioException("Não é possível excluir a recompensa '" + recompensa.getNome() +
                    "' pois ela possui " + resgatesAssociados.size() + " resgate(s) registrado(s) no histórico.");
        }

        recompensasRepository.delete(recompensa);
    }
}
