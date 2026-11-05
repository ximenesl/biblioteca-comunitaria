package br.com.senac.biblioteca.application.usecase;
import br.com.senac.biblioteca.domain.exception.BusinessConflictException;
import br.com.senac.biblioteca.domain.exception.ResourceNotFoundException;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.model.Leitor;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.ports.in.RealizarEmprestimoUseCase;
import br.com.senac.biblioteca.domain.ports.out.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
@Service
public class RealizarEmprestimoService implements RealizarEmprestimoUseCase {
    private final LivroRepositoryPort livroRepositoryPort;
    private final LeitorRepositoryPort leitorRepositoryPort;
    private final EmprestimoRepositoryPort emprestimoRepositoryPort;
    private final ClockPort clockPort;
    public RealizarEmprestimoService(LivroRepositoryPort livroRepositoryPort, LeitorRepositoryPort leitorRepositoryPort, EmprestimoRepositoryPort emprestimoRepositoryPort, ClockPort clockPort) {
        this.livroRepositoryPort = livroRepositoryPort; this.leitorRepositoryPort = leitorRepositoryPort;
        this.emprestimoRepositoryPort = emprestimoRepositoryPort; this.clockPort = clockPort;
    }
    @Override @Transactional
    public Emprestimo executar(Comando comando) {
        Livro livro = livroRepositoryPort.buscarPorId(comando.livroId()).orElseThrow(() -> new ResourceNotFoundException("Livro com ID " + comando.livroId() + " nao foi encontrado."));
        Leitor leitor = leitorRepositoryPort.buscarPorId(comando.leitorId()).orElseThrow(() -> new ResourceNotFoundException("Leitor com ID " + comando.leitorId() + " nao foi encontrado."));
        int ativosLivro = emprestimoRepositoryPort.contarEmprestimosAtivosPorLivro(livro.getId());
        if (livro.getExemplares() - ativosLivro <= 0) {
            throw new BusinessConflictException("Nao ha exemplares disponiveis para o livro '" + livro.getTitulo() + "'.");
        }
        int ativosLeitor = emprestimoRepositoryPort.contarEmprestimosAtivosPorLeitor(leitor.getId());
        if (ativosLeitor >= Emprestimo.LIMITE_MAXIMO_EMPRESTIMOS_ATIVOS) {
            throw new BusinessConflictException("O leitor '" + leitor.getNome() + "' ja possui " + ativosLeitor + " emprestimos ativos, atingindo o limite maximo de 2.");
        }
        LocalDate hoje = clockPort.dataAtual();
        return emprestimoRepositoryPort.salvar(Emprestimo.novo(livro, leitor, hoje));
    }
}