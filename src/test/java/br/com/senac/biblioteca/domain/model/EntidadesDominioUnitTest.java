package br.com.senac.biblioteca.domain.model;

import br.com.senac.biblioteca.domain.exception.BusinessConflictException;
import br.com.senac.biblioteca.domain.exception.ValidationException;
import br.com.senac.biblioteca.domain.model.vo.Isbn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntidadesDominioUnitTest {

    @Test
    @DisplayName("Livro: Deve criar livro válido com integridade de atributos")
    void deveCriarLivroValido() {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", new Isbn("978-0132350884"), 3);
        assertEquals(1L, livro.getId());
        assertEquals("Clean Code", livro.getTitulo());
        assertEquals("Robert C. Martin", livro.getAutor());
        assertEquals("978-0132350884", livro.getIsbn().valor());
        assertEquals(3, livro.getExemplares());
    }

    @Test
    @DisplayName("Livro: Deve rejeitar título vazio")
    void deveRejeitarLivroComTituloVazio() {
        assertThrows(ValidationException.class, () ->
                new Livro(1L, "  ", "Autor", new Isbn("978-0132350884"), 1));
    }

    @Test
    @DisplayName("Livro: Deve rejeitar autor vazio")
    void deveRejeitarLivroComAutorVazio() {
        assertThrows(ValidationException.class, () ->
                new Livro(1L, "Titulo", "  ", new Isbn("978-0132350884"), 1));
    }

    @Test
    @DisplayName("Livro: Deve rejeitar exemplares menor que 1")
    void deveRejeitarExemplaresMenorQueUm() {
        assertThrows(ValidationException.class, () ->
                new Livro(1L, "Titulo", "Autor", new Isbn("978-0132350884"), 0));
    }

    @Test
    @DisplayName("Leitor: Deve criar leitor válido")
    void deveCriarLeitorValido() {
        Leitor leitor = new Leitor(1L, "Ana Clara Souza");
        assertEquals(1L, leitor.getId());
        assertEquals("Ana Clara Souza", leitor.getNome());
    }

    @Test
    @DisplayName("Leitor: Deve rejeitar leitor com nome vazio")
    void deveRejeitarLeitorComNomeVazio() {
        assertThrows(ValidationException.class, () -> new Leitor(1L, "   "));
    }

    @Test
    @DisplayName("Emprestimo: Deve inicializar empréstimo ativo com zero renovações via factory method")
    void deveCriarEmprestimoValido() {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", new Isbn("978-0132350884"), 2);
        Leitor leitor = new Leitor(1L, "Lucas Ximenes");
        LocalDate hoje = LocalDate.now();

        Emprestimo emp = Emprestimo.novo(livro, leitor, hoje);

        assertNull(emp.getId());
        assertEquals(hoje, emp.getEmprestadoEm());
        assertEquals(hoje.plusDays(14), emp.getPrevistoPara());
        assertNull(emp.getDevolvidoEm());
        assertTrue(emp.isAtivo());
        assertFalse(emp.isDevolvido());
        assertEquals(0, emp.getQuantidadeRenovacoes());
    }

    @Test
    @DisplayName("Emprestimo: Devolver deve registrar data de devolução")
    void deveMarcarEmprestimoComoDevolvido() {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", new Isbn("978-0132350884"), 2);
        Leitor leitor = new Leitor(1L, "Lucas Ximenes");
        Emprestimo emp = Emprestimo.novo(livro, leitor, LocalDate.now());

        LocalDate dataDevolucao = LocalDate.now();
        emp.devolver(dataDevolucao);

        assertTrue(emp.isDevolvido());
        assertEquals(dataDevolucao, emp.getDevolvidoEm());
    }

    @Test
    @DisplayName("Emprestimo: Devolver empréstimo já devolvido deve lançar BusinessConflictException")
    void deveBloquearDevolucaoDuplaNaEntidade() {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", new Isbn("978-0132350884"), 2);
        Leitor leitor = new Leitor(1L, "Lucas Ximenes");
        Emprestimo emp = Emprestimo.novo(livro, leitor, LocalDate.now());
        emp.devolver(LocalDate.now());

        assertThrows(BusinessConflictException.class, () -> emp.devolver(LocalDate.now()));
    }

    @Test
    @DisplayName("Emprestimo: Renovar deve estender prazo em 14 dias e incrementar contador")
    void deveRenovarPrazoDeEmprestimo() {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", new Isbn("978-0132350884"), 2);
        Leitor leitor = new Leitor(1L, "Lucas Ximenes");
        LocalDate hoje = LocalDate.now();
        Emprestimo emp = Emprestimo.novo(livro, leitor, hoje);
        LocalDate prazoOriginal = emp.getPrevistoPara();

        emp.renovar(hoje);

        assertEquals(prazoOriginal.plusDays(14), emp.getPrevistoPara());
        assertEquals(1, emp.getQuantidadeRenovacoes());
    }

    @Test
    @DisplayName("Emprestimo: Renovar empréstimo atrasado deve lançar BusinessConflictException")
    void deveBloquearRenovacaoDeEmprestimoAtrasadoNaEntidade() {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", new Isbn("978-0132350884"), 2);
        Leitor leitor = new Leitor(1L, "Lucas Ximenes");
        LocalDate emprestimoAntigo = LocalDate.now().minusDays(20);
        Emprestimo emp = Emprestimo.novo(livro, leitor, emprestimoAntigo);

        assertThrows(BusinessConflictException.class, () -> emp.renovar(LocalDate.now()));
    }
}
