package br.com.senac.biblioteca.domain.ports.in;
import br.com.senac.biblioteca.domain.model.LivroComDisponibilidade;
import java.util.List;
public interface BuscarLivrosUseCase {
    List<LivroComDisponibilidade> executar(String termo);
}