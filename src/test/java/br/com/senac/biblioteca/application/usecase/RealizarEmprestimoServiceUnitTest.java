package br.com.senac.biblioteca.application.usecase;

import br.com.senac.biblioteca.domain.exception.BusinessConflictException;
import br.com.senac.biblioteca.domain.exception.ResourceNotFoundException;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.model.Leitor;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.vo.Isbn;
import br.com.senac.biblioteca.domain.ports.in.RealizarEmprestimoUseCase;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import br.com.senac.biblioteca.domain.ports.out.EmprestimoRepositoryPort;
import br.com.senac.biblioteca.domain.ports.out.LeitorRepositoryPort;
import br.com.senac.biblioteca.domain.ports.out.LivroRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RealizarEmprestimoServiceUnitTest {

    private LivroRepositoryPort livroRepository;
    private LeitorRepositoryPort leitorRepository;
    private EmprestimoRepositoryPort emprestimoRepository;
    private ClockPort clockPort;
    private RealizarEmprestimoService service;

    private Livro livro;
    private Leitor leitor;
    private final LocalDate hoje = LocalDate.of(2026, 11, 8);

    @BeforeEach
    void setUp() {
        livroRepository = mock(LivroRepositoryPort.class);
        leitorRepository = mock(LeitorRepositoryPort.class);
        emprestimoRepository = mock(EmprestimoRepositoryPort.class);
        clockPort = mock(ClockPort.class);
        service = new RealizarEmprestimoService(livroRepository, leitorRepository, emprestimoRepository, clockPort);

        livro = new Livro(1L, "Clean Architecture", "Robert C. Martin", new Isbn("978-0134494166"), 2);
        leitor = new Leitor(1L, "Mariana Silva");

        when(clockPort.dataAtual()).thenReturn(hoje);
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.of(livro));
        when(leitorRepository.buscarPorId(1L)).thenReturn(Optional.of(leitor));
    }

    @Test
    @DisplayName("CA1 & Central: Deve conceder empréstimo com sucesso e prazo de 14 dias")
    void deveRealizarEmprestimoComSucesso() {
        when(emprestimoRepository.contarEmprestimosAtivosPorLivro(1L)).thenReturn(1);
        when(emprestimoRepository.contarEmprestimosAtivosPorLeitor(1L)).thenReturn(0);
        when(emprestimoRepository.salvar(any(Emprestimo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Emprestimo resultado = service.executar(new RealizarEmprestimoUseCase.Comando(1L, 1L));

        assertNotNull(resultado);
        assertEquals(hoje, resultado.getEmprestadoEm());
        assertEquals(hoje.plusDays(14), resultado.getPrevistoPara());
        verify(emprestimoRepository).salvar(any(Emprestimo.class));
    }

    @Test
    @DisplayName("CA1: Deve lançar BusinessConflictException (409) quando estoque estiver zerado")
    void deveBloquearQuandoEstoqueEsgotado() {
        when(emprestimoRepository.contarEmprestimosAtivosPorLivro(1L)).thenReturn(2);

        BusinessConflictException ex = assertThrows(BusinessConflictException.class, () ->
                service.executar(new RealizarEmprestimoUseCase.Comando(1L, 1L)));

        assertTrue(ex.getMessage().contains("Nao ha exemplares disponiveis"));
        verify(emprestimoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("CA2: Deve lançar BusinessConflictException (409) quando leitor tiver 2 empréstimos ativos")
    void deveBloquearQuandoLeitorAtingirLimiteDeDois() {
        when(emprestimoRepository.contarEmprestimosAtivosPorLivro(1L)).thenReturn(0);
        when(emprestimoRepository.contarEmprestimosAtivosPorLeitor(1L)).thenReturn(2);

        BusinessConflictException ex = assertThrows(BusinessConflictException.class, () ->
                service.executar(new RealizarEmprestimoUseCase.Comando(1L, 1L)));

        assertTrue(ex.getMessage().contains("limite maximo de 2"));
        verify(emprestimoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando livro não for encontrado")
    void deveFalharQuandoLivroInexistente() {
        when(livroRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.executar(new RealizarEmprestimoUseCase.Comando(99L, 1L)));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando leitor não for encontrado")
    void deveFalharQuandoLeitorInexistente() {
        when(leitorRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.executar(new RealizarEmprestimoUseCase.Comando(1L, 99L)));
    }
}

