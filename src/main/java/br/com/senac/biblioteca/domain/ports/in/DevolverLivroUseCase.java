package br.com.senac.biblioteca.domain.ports.in;
import br.com.senac.biblioteca.domain.model.Emprestimo;
public interface DevolverLivroUseCase {
    Emprestimo executar(Long emprestimoId);
}