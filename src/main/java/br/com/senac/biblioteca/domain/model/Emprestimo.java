package br.com.senac.biblioteca.domain.model;
import br.com.senac.biblioteca.domain.exception.BusinessConflictException;
import br.com.senac.biblioteca.domain.exception.ValidationException;
import java.time.LocalDate;
import java.util.Objects;
public class Emprestimo {
    public static final int PRAZO_PADRAO_DIAS = 14;
    public static final int LIMITE_MAXIMO_EMPRESTIMOS_ATIVOS = 2;

    private final Long id;
    private final Livro livro;
    private final Leitor leitor;
    private final LocalDate emprestadoEm;
    private LocalDate previstoPara;
    private LocalDate devolvidoEm;
    private int quantidadeRenovacoes;

    public Emprestimo(Long id, Livro livro, Leitor leitor, LocalDate emprestadoEm, LocalDate previstoPara, LocalDate devolvidoEm, int quantidadeRenovacoes) {
        if (livro == null || leitor == null || emprestadoEm == null || previstoPara == null) {
            throw new ValidationException("Dados obrigatorios do emprestimo nao informados.");
        }
        this.id = id;
        this.livro = livro;
        this.leitor = leitor;
        this.emprestadoEm = emprestadoEm;
        this.previstoPara = previstoPara;
        this.devolvidoEm = devolvidoEm;
        this.quantidadeRenovacoes = Math.max(0, quantidadeRenovacoes);
    }
    public static Emprestimo novo(Livro livro, Leitor leitor, LocalDate dataEmprestimo) {
        return new Emprestimo(null, livro, leitor, dataEmprestimo, dataEmprestimo.plusDays(PRAZO_PADRAO_DIAS), null, 0);
    }
    public boolean isAtivo() { return this.devolvidoEm == null; }
    public boolean isDevolvido() { return this.devolvidoEm != null; }
    public boolean isAtrasado(LocalDate dataReferencia) { return isAtivo() && this.previstoPara.isBefore(dataReferencia); }
    public void devolver(LocalDate dataDevolucao) {
        if (isDevolvido()) throw new BusinessConflictException("Este emprestimo ja foi devolvido anteriormente.");
        if (dataDevolucao == null) throw new ValidationException("Data de devolucao obrigatoria.");
        this.devolvidoEm = dataDevolucao;
    }
    public void renovar(LocalDate dataReferencia) {
        if (!isAtivo()) throw new BusinessConflictException("Nao e possivel renovar emprestimo ja devolvido.");
        if (isAtrasado(dataReferencia)) throw new BusinessConflictException("Nao e permitida a renovacao de emprestimos em atraso.");
        if (this.quantidadeRenovacoes >= 1) throw new BusinessConflictException("Limite maximo de 1 renovacao atingido.");
        this.previstoPara = this.previstoPara.plusDays(PRAZO_PADRAO_DIAS);
        this.quantidadeRenovacoes++;
    }
    public Long getId() { return id; }
    public Livro getLivro() { return livro; }
    public Leitor getLeitor() { return leitor; }
    public LocalDate getEmprestadoEm() { return emprestadoEm; }
    public LocalDate getPrevistoPara() { return previstoPara; }
    public LocalDate getDevolvidoEm() { return devolvidoEm; }
    public int getQuantidadeRenovacoes() { return quantidadeRenovacoes; }
}