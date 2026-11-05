package br.com.senac.biblioteca.infrastructure.adapters.out.persistence;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.vo.Isbn;
import br.com.senac.biblioteca.domain.ports.out.LivroRepositoryPort;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.LivroJpaEntity;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.mapper.BibliotecaPersistenceMapper;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository.SpringDataLivroRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
@Component
public class LivroPersistenceAdapter implements LivroRepositoryPort {
    private final SpringDataLivroRepository repository;
    private final BibliotecaPersistenceMapper mapper;
    public LivroPersistenceAdapter(SpringDataLivroRepository repository, BibliotecaPersistenceMapper mapper) {
        this.repository = repository; this.mapper = mapper;
    }
    @Override public Livro salvar(Livro livro) { return mapper.toDomain(repository.save(mapper.toJpaEntity(livro))); }
    @Override public Optional<Livro> buscarPorId(Long id) { return repository.findById(id).map(mapper::toDomain); }
    @Override public Optional<Livro> buscarPorIsbn(Isbn isbn) { return repository.findByIsbn(isbn.valor()).map(mapper::toDomain); }
    @Override public boolean existePorIsbn(Isbn isbn) { return repository.existsByIsbn(isbn.valor()); }
    @Override public List<Livro> listarTodos() { return repository.findAll().stream().map(mapper::toDomain).toList(); }
    @Override public List<Livro> buscarPorTermo(String termo) { return repository.buscarPorTermo(termo).stream().map(mapper::toDomain).toList(); }
}