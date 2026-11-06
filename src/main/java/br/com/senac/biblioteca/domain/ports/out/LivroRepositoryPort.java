package br.com.senac.biblioteca.domain.ports.out;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.vo.Isbn;
import java.util.List;
import java.util.Optional;
public interface LivroRepositoryPort {
    Livro salvar(Livro livro);
    Optional<Livro> buscarPorId(Long id);
    Optional<Livro> buscarPorIsbn(Isbn isbn);
    boolean existePorIsbn(Isbn isbn);
    List<Livro> listarTodos();
    List<Livro> buscarPorTermo(String termo);
}