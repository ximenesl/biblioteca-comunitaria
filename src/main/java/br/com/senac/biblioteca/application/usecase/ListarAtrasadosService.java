package br.com.senac.biblioteca.application.usecase;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.ports.in.ListarAtrasadosUseCase;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import br.com.senac.biblioteca.domain.ports.out.EmprestimoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ListarAtrasadosService implements ListarAtrasadosUseCase {
    private final EmprestimoRepositoryPort emprestimoRepositoryPort;
    private final ClockPort clockPort;
    public ListarAtrasadosService(EmprestimoRepositoryPort emprestimoRepositoryPort, ClockPort clockPort) {
        this.emprestimoRepositoryPort = emprestimoRepositoryPort; this.clockPort = clockPort;
    }
    @Override @Transactional(readOnly = true)
    public List<Emprestimo> executar() { return emprestimoRepositoryPort.buscarAtrasados(clockPort.dataAtual()); }
}