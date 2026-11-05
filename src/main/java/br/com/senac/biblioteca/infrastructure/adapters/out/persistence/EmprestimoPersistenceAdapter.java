package br.com.senac.biblioteca.infrastructure.adapters.out.persistence;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.model.RankingLivro;
import br.com.senac.biblioteca.domain.ports.out.EmprestimoRepositoryPort;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.mapper.BibliotecaPersistenceMapper;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository.SpringDataEmprestimoRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Component
public class EmprestimoPersistenceAdapter implements EmprestimoRepositoryPort {
    private final SpringDataEmprestimoRepository repository;
    private final BibliotecaPersistenceMapper mapper;
    public EmprestimoPersistenceAdapter(SpringDataEmprestimoRepository repository, BibliotecaPersistenceMapper mapper) {
        this.repository = repository; this.mapper = mapper;
    }
    @Override public Emprestimo salvar(Emprestimo emprestimo) { return mapper.toDomain(repository.save(mapper.toJpaEntity(emprestimo))); }
    @Override public Optional<Emprestimo> buscarPorId(Long id) { return repository.findById(id).map(mapper::toDomain); }
    @Override public int contarEmprestimosAtivosPorLeitor(Long leitorId) { return repository.contarAtivosPorLeitor(leitorId); }
    @Override public int contarEmprestimosAtivosPorLivro(Long livroId) { return repository.contarAtivosPorLivro(livroId); }
    @Override public List<Emprestimo> buscarAtrasados(LocalDate dataReferencia) { return repository.buscarAtrasados(dataReferencia).stream().map(mapper::toDomain).toList(); }
    @Override public List<Emprestimo> buscarPorLeitor(Long leitorId) { return repository.buscarPorLeitor(leitorId).stream().map(mapper::toDomain).toList(); }
    @Override public List<RankingLivro> obterRankingMaisEmprestados() { return repository.obterRankingMaisEmprestados(); }
}