package br.com.senac.biblioteca.application.usecase;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.LivroComDisponibilidade;
import br.com.senac.biblioteca.domain.ports.in.BuscarLivrosUseCase;
import br.com.senac.biblioteca.domain.ports.out.EmprestimoRepositoryPort;
import br.com.senac.biblioteca.domain.ports.out.LivroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class BuscarLivrosService implements BuscarLivrosUseCase {
    private final LivroRepositoryPort livroRepositoryPort;
    private final EmprestimoRepositoryPort emprestimoRepositoryPort;
    public BuscarLivrosService(LivroRepositoryPort livroRepositoryPort, EmprestimoRepositoryPort emprestimoRepositoryPort) {
        this.livroRepositoryPort = livroRepositoryPort; this.emprestimoRepositoryPort = emprestimoRepositoryPort;
    }
    @Override @Transactional(readOnly = true)
    public List<LivroComDisponibilidade> executar(String termo) {
        List<Livro> livros = (termo == null || termo.isBlank()) ? livroRepositoryPort.listarTodos() : livroRepositoryPort.buscarPorTermo(termo.trim());
        return livros.stream().map(l -> {
            int ativos = emprestimoRepositoryPort.contarEmprestimosAtivosPorLivro(l.getId());
            int disponiveis = Math.max(0, l.getExemplares() - ativos);
            return new LivroComDisponibilidade(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn().valor(), l.getExemplares(), disponiveis);
        }).toList();
    }
}