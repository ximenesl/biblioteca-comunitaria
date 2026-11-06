package br.com.senac.biblioteca.application.usecase;
import br.com.senac.biblioteca.domain.exception.ResourceNotFoundException;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.ports.in.RenovarEmprestimoUseCase;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import br.com.senac.biblioteca.domain.ports.out.EmprestimoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class RenovarEmprestimoService implements RenovarEmprestimoUseCase {
    private final EmprestimoRepositoryPort emprestimoRepositoryPort;
    private final ClockPort clockPort;
    public RenovarEmprestimoService(EmprestimoRepositoryPort emprestimoRepositoryPort, ClockPort clockPort) {
        this.emprestimoRepositoryPort = emprestimoRepositoryPort; this.clockPort = clockPort;
    }
    @Override @Transactional
    public Emprestimo executar(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepositoryPort.buscarPorId(emprestimoId).orElseThrow(() -> new ResourceNotFoundException("Emprestimo com ID " + emprestimoId + " nao foi encontrado."));
        emprestimo.renovar(clockPort.dataAtual());
        return emprestimoRepositoryPort.salvar(emprestimo);
    }
}