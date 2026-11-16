package com.ambientiza.api.services;

import com.ambientiza.api.dtos.recompensas.CadastroRecompensa;
import com.ambientiza.api.dtos.recompensas.RecompensaResponse;
import com.ambientiza.api.dtos.recompensas.ResgateRequest;
import com.ambientiza.api.dtos.recompensas.ResgateResponse;
import com.ambientiza.api.exceptions.EstoqueInsuficienteException;
import com.ambientiza.api.exceptions.RegraDeNegocioException;
import com.ambientiza.api.exceptions.SaldoInsuficienteException;
import com.ambientiza.api.models.Recompensas;
import com.ambientiza.api.models.ResgateRecompensas;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.RecompensasRepository;
import com.ambientiza.api.repositories.ResgateRecompensaRepository;
import com.ambientiza.api.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecompensaServiceTest {

    @Mock
    private RecompensasRepository recompensasRepository;

    @Mock
    private ResgateRecompensaRepository resgateRecompensaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RecompensaService recompensaService;

    private Usuarios usuario;
    private Recompensas recompensa;

    @BeforeEach
    void setUp() {
        usuario = new Usuarios();
        usuario.setIdUsuario(1L);
        usuario.setNome("Rodrigo Silva");
        usuario.setEmail("rodrigo@email.com");
        usuario.setSaldo(new BigDecimal("150.00"));

        recompensa = new Recompensas();
        recompensa.setIdRecompensa(10L);
        recompensa.setNome("Muda de Árvore Nativa");
        recompensa.setDescricao("Certificado de plantio de uma muda nativa em área de reflorestamento.");
        recompensa.setCustoPontos(new BigDecimal("100.00"));
        recompensa.setEstoque(5);
    }

    @Test
    @DisplayName("Deve cadastrar uma recompensa com sucesso")
    void cadastrarRecompensaSucesso() {
        CadastroRecompensa dto = new CadastroRecompensa(
                "Garrafa Ecológica",
                "Garrafa de alumínio reciclado",
                new BigDecimal("80.00"),
                10
        );

        when(recompensasRepository.existsByNome("Garrafa Ecológica")).thenReturn(false);
        when(recompensasRepository.save(any(Recompensas.class))).thenAnswer(invocation -> {
            Recompensas r = invocation.getArgument(0);
            r.setIdRecompensa(20L);
            return r;
        });

        RecompensaResponse response = recompensaService.cadastrar(dto);

        assertNotNull(response);
        assertEquals(20L, response.idRecompensa());
        assertEquals("Garrafa Ecológica", response.nome());
        assertEquals(10, response.estoque());
        assertTrue(response.disponivel());
        verify(recompensasRepository, times(1)).save(any(Recompensas.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar recompensa com nome duplicado")
    void cadastrarRecompensaNomeDuplicado() {
        CadastroRecompensa dto = new CadastroRecompensa(
                "Muda de Árvore Nativa",
                "Descrição",
                new BigDecimal("100.00"),
                5
        );

        when(recompensasRepository.existsByNome("Muda de Árvore Nativa")).thenReturn(true);

        assertThrows(RegraDeNegocioException.class, () -> recompensaService.cadastrar(dto));
        verify(recompensasRepository, never()).save(any(Recompensas.class));
    }

    @Test
    @DisplayName("Deve realizar resgate com sucesso deduzindo saldo e decrementando estoque")
    void resgatarSucesso() {
        ResgateRequest request = new ResgateRequest(1L, 10L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(recompensasRepository.findById(10L)).thenReturn(Optional.of(recompensa));
        when(resgateRecompensaRepository.save(any(ResgateRecompensas.class))).thenAnswer(invocation -> {
            ResgateRecompensas rr = invocation.getArgument(0);
            rr.setIdResgate(100L);
            return rr;
        });

        ResgateResponse response = recompensaService.resgatar(request);

        assertNotNull(response);
        assertEquals(100L, response.idResgate());
        assertEquals("Muda de Árvore Nativa", response.nomeRecompensa());
        assertEquals("Rodrigo Silva", response.nomeUsuario());
        assertEquals(new BigDecimal("100.00"), response.pontosGastos());
        assertEquals(new BigDecimal("50.00"), response.saldoRestante());
        assertEquals(4, recompensa.getEstoque()); // Estoque decrementado de 5 para 4
        assertEquals(new BigDecimal("50.00"), usuario.getSaldo()); // Saldo deduzido de 150 para 50

        verify(usuarioRepository, times(1)).save(usuario);
        verify(recompensasRepository, times(1)).save(recompensa);
        verify(resgateRecompensaRepository, times(1)).save(any(ResgateRecompensas.class));
    }

    @Test
    @DisplayName("Deve lançar EstoqueInsuficienteException quando estoque for zero")
    void resgatarEstoqueZero() {
        recompensa.setEstoque(0);
        ResgateRequest request = new ResgateRequest(1L, 10L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(recompensasRepository.findById(10L)).thenReturn(Optional.of(recompensa));

        assertThrows(EstoqueInsuficienteException.class, () -> recompensaService.resgatar(request));

        verify(usuarioRepository, never()).save(any(Usuarios.class));
        verify(recompensasRepository, never()).save(any(Recompensas.class));
        verify(resgateRecompensaRepository, never()).save(any(ResgateRecompensas.class));
    }

    @Test
    @DisplayName("Deve lançar SaldoInsuficienteException quando usuário não tiver pontos suficientes")
    void resgatarSaldoInsuficiente() {
        usuario.setSaldo(new BigDecimal("30.00")); // Custo é 100.00
        ResgateRequest request = new ResgateRequest(1L, 10L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(recompensasRepository.findById(10L)).thenReturn(Optional.of(recompensa));

        assertThrows(SaldoInsuficienteException.class, () -> recompensaService.resgatar(request));

        assertEquals(5, recompensa.getEstoque()); // Estoque permanece 5
        assertEquals(new BigDecimal("30.00"), usuario.getSaldo()); // Saldo não é alterado

        verify(usuarioRepository, never()).save(any(Usuarios.class));
        verify(recompensasRepository, never()).save(any(Recompensas.class));
        verify(resgateRecompensaRepository, never()).save(any(ResgateRecompensas.class));
    }

    @Test
    @DisplayName("Deve adicionar itens ao estoque com sucesso")
    void adicionarEstoqueSucesso() {
        when(recompensasRepository.findById(10L)).thenReturn(Optional.of(recompensa));
        when(recompensasRepository.save(any(Recompensas.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecompensaResponse response = recompensaService.adicionarEstoque(10L, 10);

        assertEquals(15, response.estoque()); // 5 + 10 = 15
        verify(recompensasRepository, times(1)).save(recompensa);
    }

    @Test
    @DisplayName("Deve impedir exclusão de recompensa que possui histórico de resgate")
    void deletarRecompensaComHistorico() {
        ResgateRecompensas resgateExistente = new ResgateRecompensas(usuario, recompensa, recompensa.getCustoPontos());

        when(recompensasRepository.findById(10L)).thenReturn(Optional.of(recompensa));
        when(resgateRecompensaRepository.findByRecompensaIdRecompensaOrderByDataResgateDesc(10L))
                .thenReturn(List.of(resgateExistente));

        assertThrows(RegraDeNegocioException.class, () -> recompensaService.deletar(10L));
        verify(recompensasRepository, never()).delete(any(Recompensas.class));
    }
}
