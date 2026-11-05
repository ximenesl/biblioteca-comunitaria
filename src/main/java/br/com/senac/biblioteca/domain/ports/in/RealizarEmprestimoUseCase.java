package br.com.senac.biblioteca.domain.ports.in;
import br.com.senac.biblioteca.domain.model.Emprestimo;
public interface RealizarEmprestimoUseCase {
    Emprestimo executar(Comando comando);
    record Comando(Long livroId, Long leitorId) {}
}