package com.ambientiza.api.services;

import com.ambientiza.api.dtos.usuarios.CadastroUsuario;
import com.ambientiza.api.dtos.usuarios.LoginUsuario;
import com.ambientiza.api.dtos.usuarios.OfensivaResponse;
import com.ambientiza.api.dtos.usuarios.UsuarioResponse;
import com.ambientiza.api.exceptions.RegraDeNegocioException;
import com.ambientiza.api.exceptions.SaldoInsuficienteException;
import com.ambientiza.api.models.Usuarios;
import com.ambientiza.api.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuarios usuarioExemplo;

    @BeforeEach
    void setUp() {
        usuarioExemplo = new Usuarios();
        usuarioExemplo.setIdUsuario(1L);
        usuarioExemplo.setNome("Rodrigo Silva");
        usuarioExemplo.setEmail("rodrigo@email.com");
        // Hash de "senha123" usando SHA-256
        usuarioExemplo.setSenhaHash("55525e1b3ab5b27dafb13233e6524f1499d07d187176574c838ac6b0736e3810");
        usuarioExemplo.setSaldo(new BigDecimal("100.00"));
        usuarioExemplo.setOfensivaAtual(3);
        usuarioExemplo.setUltimaAtividade(LocalDate.now().minusDays(1));
        usuarioExemplo.setCriadoEm(LocalDate.now().minusDays(10));
    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void cadastrarSucesso() {
        CadastroUsuario dto = new CadastroUsuario("Novo Aluno", "aluno@email.com", "senhaForte123");

        when(usuarioRepository.existsByEmail("aluno@email.com")).thenReturn(false);
        when(usuarioRepository.existsByNome("Novo Aluno")).thenReturn(false);
        when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> {
            Usuarios u = invocation.getArgument(0);
            u.setIdUsuario(2L);
            return u;
        });

        UsuarioResponse response = usuarioService.cadastrar(dto);

        assertNotNull(response);
        assertEquals(2L, response.idUsuario());
        assertEquals("Novo Aluno", response.nome());
        assertEquals("aluno@email.com", response.email());
        assertEquals(BigDecimal.ZERO, response.saldo());
        assertEquals(0, response.ofensivaAtual());
        verify(usuarioRepository, times(1)).save(any(Usuarios.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar email já existente")
    void cadastrarEmailDuplicado() {
        CadastroUsuario dto = new CadastroUsuario("Outro Aluno", "rodrigo@email.com", "senha123");
        when(usuarioRepository.existsByEmail("rodrigo@email.com")).thenReturn(true);

        assertThrows(RegraDeNegocioException.class, () -> usuarioService.cadastrar(dto));
        verify(usuarioRepository, never()).save(any(Usuarios.class));
    }

    @Test
    @DisplayName("Deve autenticar login com sucesso por email")
    void loginComSucessoPorEmail() {
        LoginUsuario dto = new LoginUsuario("rodrigo@email.com", "senha123");
        when(usuarioRepository.findByEmail("rodrigo@email.com")).thenReturn(Optional.of(usuarioExemplo));

        UsuarioResponse response = usuarioService.login(dto);

        assertNotNull(response);
        assertEquals("Rodrigo Silva", response.nome());
        assertEquals("rodrigo@email.com", response.email());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar login com senha incorreta")
    void loginSenhaIncorreta() {
        LoginUsuario dto = new LoginUsuario("rodrigo@email.com", "senhaErrada");
        when(usuarioRepository.findByEmail("rodrigo@email.com")).thenReturn(Optional.of(usuarioExemplo));

        assertThrows(RegraDeNegocioException.class, () -> usuarioService.login(dto));
    }

    @Test
    @DisplayName("Deve adicionar pontos ao saldo com sucesso")
    void adicionarPontosSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExemplo));
        when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponse response = usuarioService.adicionarPontos(1L, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), response.saldo());
    }

    @Test
    @DisplayName("Deve debitar pontos com sucesso quando há saldo suficiente")
    void debitarPontosComSaldoSuficiente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExemplo));
        when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponse response = usuarioService.debitarPontos(1L, new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), response.saldo());
    }

    @Test
    @DisplayName("Deve lançar SaldoInsuficienteException ao tentar debitar mais do que o saldo")
    void debitarPontosSaldoInsuficiente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExemplo));

        assertThrows(SaldoInsuficienteException.class, () ->
                usuarioService.debitarPontos(1L, new BigDecimal("200.00"))
        );
        verify(usuarioRepository, never()).save(any(Usuarios.class));
    }

    @Test
    @DisplayName("Deve incrementar ofensiva em dia consecutivo (ontem -> hoje)")
    void incrementarOfensivaConsecutiva() {
        usuarioExemplo.setUltimaAtividade(LocalDate.now().minusDays(1));
        usuarioExemplo.setOfensivaAtual(5);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExemplo));
        when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OfensivaResponse response = usuarioService.registrarAtividadeEAtualizarOfensiva(1L);

        assertEquals(6, response.ofensivaAtual());
        assertTrue(response.atividadeRealizadaHoje());
    }

    @Test
    @DisplayName("Deve manter ofensiva inalterada se já realizou atividade hoje")
    void manterOfensivaMesmoDia() {
        usuarioExemplo.setUltimaAtividade(LocalDate.now());
        usuarioExemplo.setOfensivaAtual(5);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExemplo));
        when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OfensivaResponse response = usuarioService.registrarAtividadeEAtualizarOfensiva(1L);

        assertEquals(5, response.ofensivaAtual());
        assertTrue(response.atividadeRealizadaHoje());
    }

    @Test
    @DisplayName("Deve reiniciar ofensiva para 1 se perdeu um dia")
    void reiniciarOfensivaAposQuebra() {
        usuarioExemplo.setUltimaAtividade(LocalDate.now().minusDays(3)); // 3 dias sem jogar
        usuarioExemplo.setOfensivaAtual(10);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExemplo));
        when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OfensivaResponse response = usuarioService.registrarAtividadeEAtualizarOfensiva(1L);

        assertEquals(1, response.ofensivaAtual());
        assertTrue(response.atividadeRealizadaHoje());
    }
}
