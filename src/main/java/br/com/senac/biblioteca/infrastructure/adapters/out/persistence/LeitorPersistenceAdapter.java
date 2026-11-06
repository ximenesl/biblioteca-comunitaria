package br.com.senac.biblioteca.infrastructure.adapters.out.persistence;
import br.com.senac.biblioteca.domain.model.Leitor;
import br.com.senac.biblioteca.domain.ports.out.LeitorRepositoryPort;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.mapper.BibliotecaPersistenceMapper;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository.SpringDataLeitorRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
@Component
public class LeitorPersistenceAdapter implements LeitorRepositoryPort {
    private final SpringDataLeitorRepository repository;
    private final BibliotecaPersistenceMapper mapper;
    public LeitorPersistenceAdapter(SpringDataLeitorRepository repository, BibliotecaPersistenceMapper mapper) {
        this.repository = repository; this.mapper = mapper;
    }
    @Override public Leitor salvar(Leitor leitor) { return mapper.toDomain(repository.save(mapper.toJpaEntity(leitor))); }
    @Override public Optional<Leitor> buscarPorId(Long id) { return repository.findById(id).map(mapper::toDomain); }
    @Override public List<Leitor> listarTodos() { return repository.findAll().stream().map(mapper::toDomain).toList(); }
}