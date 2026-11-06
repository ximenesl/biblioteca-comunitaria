package br.com.senac.biblioteca.application.usecase;
import br.com.senac.biblioteca.domain.model.RankingLivro;
import br.com.senac.biblioteca.domain.ports.in.ObterRankingLivrosUseCase;
import br.com.senac.biblioteca.domain.ports.out.EmprestimoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ObterRankingLivrosService implements ObterRankingLivrosUseCase {
    private final EmprestimoRepositoryPort emprestimoRepositoryPort;
    public ObterRankingLivrosService(EmprestimoRepositoryPort emprestimoRepositoryPort) { this.emprestimoRepositoryPort = emprestimoRepositoryPort; }
    @Override @Transactional(readOnly = true)
    public List<RankingLivro> executar() { return emprestimoRepositoryPort.obterRankingMaisEmprestados(); }
}