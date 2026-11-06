package br.com.senac.biblioteca.application.usecase;
import br.com.senac.biblioteca.domain.exception.ResourceNotFoundException;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.ports.in.ConsultarHistoricoLeitorUseCase;
import br.com.senac.biblioteca.domain.ports.out.EmprestimoRepositoryPort;
import br.com.senac.biblioteca.domain.ports.out.LeitorRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ConsultarHistoricoLeitorService implements ConsultarHistoricoLeitorUseCase {
    private final LeitorRepositoryPort leitorRepositoryPort;
    private final EmprestimoRepositoryPort emprestimoRepositoryPort;
    public ConsultarHistoricoLeitorService(LeitorRepositoryPort leitorRepositoryPort, EmprestimoRepositoryPort emprestimoRepositoryPort) {
        this.leitorRepositoryPort = leitorRepositoryPort; this.emprestimoRepositoryPort = emprestimoRepositoryPort;
    }
    @Override @Transactional(readOnly = true)
    public List<Emprestimo> executar(Long leitorId) {
        leitorRepositoryPort.buscarPorId(leitorId).orElseThrow(() -> new ResourceNotFoundException("Leitor com ID " + leitorId + " nao foi encontrado."));
        return emprestimoRepositoryPort.buscarPorLeitor(leitorId);
    }
}