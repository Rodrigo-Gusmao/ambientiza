package com.ambientiza.api.services;

import com.ambientiza.api.dtos.Requisicoes.RecompensaRequest;
import com.ambientiza.api.dtos.Respostas.RecompensaResponse;
import com.ambientiza.api.dtos.Respostas.UsuarioResponse;
import com.ambientiza.api.exceptions.ApiException;
import com.ambientiza.api.models.Recompensas;
import com.ambientiza.api.models.ResgateRecompensas;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.RecompensasRepository;
import com.ambientiza.api.repositories.ResgateRecompensaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecompensaService {
    private final RecompensasRepository recompensasRepository;
    private final ResgateRecompensaRepository resgateRepository;
    private final UsuarioService usuarioService;

    public RecompensaService(RecompensasRepository recompensasRepository, ResgateRecompensaRepository resgateRepository,
                             UsuarioService usuarioService) {
        this.recompensasRepository = recompensasRepository;
        this.resgateRepository = resgateRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<RecompensaResponse> listar() {
        return recompensasRepository.findAll().stream().map(RecompensaResponse::de).toList();
    }

    @Transactional
    public UsuarioResponse resgatar(Long idUsuario, Long idRecompensa) {
        Usuarios usuario = usuarioService.obterParaAtualizar(idUsuario);
        Recompensas r = recompensasRepository.findByIdParaAtualizar(idRecompensa)
                .orElseThrow(() -> ApiException.naoEncontrado("Recompensa não encontrada."));
        if (r.getEstoque() <= 0) throw ApiException.conflito("Recompensa esgotada.");
        if (usuario.getSaldo() < r.getCusto()) throw ApiException.conflito("Saldo insuficiente.");

        r.setEstoque(r.getEstoque() - 1);
        usuario.setSaldo(usuario.getSaldo() - r.getCusto());

        ResgateRecompensas resgate = new ResgateRecompensas();
        resgate.setUsuario(usuario);
        resgate.setRecompensa(r);
        resgate.setPontosGastos(r.getCusto());
        resgate.setDataResgate(LocalDate.now());
        resgateRepository.save(resgate);

        return usuarioService.paraResponse(usuario);
    }

    @Transactional
    public RecompensaResponse salvar(Long idRecompensa, RecompensaRequest req) {
        Recompensas r = idRecompensa == null ? new Recompensas() : obter(idRecompensa);
        r.setNome(req.nome().trim());
        r.setDescricao(req.descricao().trim());
        r.setCusto(req.custo());
        r.setEstoque(req.estoque());
        return RecompensaResponse.de(recompensasRepository.save(r));
    }

    @Transactional
    public void remover(Long idRecompensa) {
        Recompensas r = obter(idRecompensa);
        if (resgateRepository.existsByRecompensaIdRecompensa(idRecompensa)) {
            throw ApiException.conflito("Recompensa já foi resgatada; zere o estoque para retirá-la da loja.");
        }
        recompensasRepository.delete(r);
    }

    private Recompensas obter(Long idRecompensa) {
        return recompensasRepository.findById(idRecompensa)
                .orElseThrow(() -> ApiException.naoEncontrado("Recompensa não encontrada."));
    }
}
