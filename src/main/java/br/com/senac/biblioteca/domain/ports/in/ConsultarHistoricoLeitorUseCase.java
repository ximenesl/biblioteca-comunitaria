package br.com.senac.biblioteca.domain.ports.in;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import java.util.List;
public interface ConsultarHistoricoLeitorUseCase {
    List<Emprestimo> executar(Long leitorId);
}