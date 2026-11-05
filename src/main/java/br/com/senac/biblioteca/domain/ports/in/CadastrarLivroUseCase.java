package br.com.senac.biblioteca.domain.ports.in;
import br.com.senac.biblioteca.domain.model.Livro;
public interface CadastrarLivroUseCase {
    Livro executar(Comando comando);
    record Comando(String titulo, String autor, String isbn, int exemplares) {}
}