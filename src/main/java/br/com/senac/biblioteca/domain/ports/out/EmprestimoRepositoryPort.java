package br.com.senac.biblioteca.domain.ports.out;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.model.RankingLivro;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface EmprestimoRepositoryPort {
    Emprestimo salvar(Emprestimo emprestimo);
    Optional<Emprestimo> buscarPorId(Long id);
    int contarEmprestimosAtivosPorLeitor(Long leitorId);
    int contarEmprestimosAtivosPorLivro(Long livroId);
    List<Emprestimo> buscarAtrasados(LocalDate dataReferencia);
    List<Emprestimo> buscarPorLeitor(Long leitorId);
    List<RankingLivro> obterRankingMaisEmprestados();
}