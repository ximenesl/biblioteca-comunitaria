package br.com.senac.biblioteca.domain.ports.out;
import br.com.senac.biblioteca.domain.model.Leitor;
import java.util.List;
import java.util.Optional;
public interface LeitorRepositoryPort {
    Leitor salvar(Leitor leitor);
    Optional<Leitor> buscarPorId(Long id);
    List<Leitor> listarTodos();
}