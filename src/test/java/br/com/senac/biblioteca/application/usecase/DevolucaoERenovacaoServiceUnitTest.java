package br.com.senac.biblioteca.application.usecase;

import br.com.senac.biblioteca.domain.exception.BusinessConflictException;
import br.com.senac.biblioteca.domain.exception.ResourceNotFoundException;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.model.Leitor;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.vo.Isbn;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import br.com.senac.biblioteca.domain.ports.out.EmprestimoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DevolucaoERenovacaoServiceUnitTest {

    private EmprestimoRepositoryPort emprestimoRepository;
    private ClockPort clockPort;
    private DevolverLivroService devolverService;
    private RenovarEmprestimoService renovarService;

    private Livro livro;
    private Leitor leitor;
    private final LocalDate hoje = LocalDate.of(2026, 11, 8);

    @BeforeEach
    void setUp() {
        emprestimoRepository = mock(EmprestimoRepositoryPort.class);
        clockPort = mock(ClockPort.class);
        devolverService = new DevolverLivroService(emprestimoRepository, clockPort);
        renovarService = new RenovarEmprestimoService(emprestimoRepository, clockPort);

        livro = new Livro(1L, "Domain-Driven Design", "Eric Evans", new Isbn("978-0321125217"), 1);
        leitor = new Leitor(1L, "Carlos Eduardo");
        when(clockPort.dataAtual()).thenReturn(hoje);
    }

    @Test
    @DisplayName("CA3: Deve realizar devolução de empréstimo ativo com sucesso")
    void deveDevolverEmprestimoAtivo() {
        Emprestimo emp = new Emprestimo(10L, livro, leitor, hoje.minusDays(5), hoje.plusDays(9), null, 0);
        when(emprestimoRepository.buscarPorId(10L)).thenReturn(Optional.of(emp));
        when(emprestimoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Emprestimo devolvido = devolverService.executar(10L);

        assertTrue(devolvido.isDevolvido());
        assertEquals(hoje, devolvido.getDevolvidoEm());
        verify(emprestimoRepository).salvar(emp);
    }

    @Test
    @DisplayName("CA3: Deve lançar BusinessConflictException (409) ao tentar devolver empréstimo já devolvido")
    void deveBloquearDevolucaoDupla() {
        Emprestimo emp = new Emprestimo(10L, livro, leitor, hoje.minusDays(10), hoje.plusDays(4), null, 0);
        emp.devolver(hoje.minusDays(2));
        when(emprestimoRepository.buscarPorId(10L)).thenReturn(Optional.of(emp));

        BusinessConflictException ex = assertThrows(BusinessConflictException.class, () ->
                devolverService.executar(10L));

        assertTrue(ex.getMessage().contains("ja foi devolvido"));
        verify(emprestimoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve falhar com ResourceNotFoundException ao devolver empréstimo inexistente")
    void deveFalharDevolucaoEmprestimoInexistente() {
        when(emprestimoRepository.buscarPorId(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> devolverService.executar(99L));
    }

    @Test
    @DisplayName("Renovação: Deve prorrogar prazo em +14 dias corridos")
    void deveRenovarEmprestimoSemAtraso() {
        LocalDate prazoOriginal = hoje.plusDays(5);
        Emprestimo emp = new Emprestimo(10L, livro, leitor, hoje.minusDays(9), prazoOriginal, null, 0);
        when(emprestimoRepository.buscarPorId(10L)).thenReturn(Optional.of(emp));
        when(emprestimoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Emprestimo renovado = renovarService.executar(10L);

        assertEquals(prazoOriginal.plusDays(14), renovado.getPrevistoPara());
        assertEquals(1, renovado.getQuantidadeRenovacoes());
        verify(emprestimoRepository).salvar(emp);
    }

    @Test
    @DisplayName("Renovação: Deve lançar BusinessConflictException (409) quando empréstimo estiver atrasado")
    void deveBloquearRenovacaoDeEmprestimoAtrasado() {
        LocalDate prazoVencido = hoje.minusDays(2);
        Emprestimo emp = new Emprestimo(10L, livro, leitor, hoje.minusDays(16), prazoVencido, null, 0);
        when(emprestimoRepository.buscarPorId(10L)).thenReturn(Optional.of(emp));

        BusinessConflictException ex = assertThrows(BusinessConflictException.class, () ->
                renovarService.executar(10L));

        assertTrue(ex.getMessage().contains("em atraso"));
        verify(emprestimoRepository, never()).salvar(any());
    }
}

