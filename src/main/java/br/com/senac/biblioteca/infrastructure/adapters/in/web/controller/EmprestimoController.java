package br.com.senac.biblioteca.infrastructure.adapters.in.web.controller;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.ports.in.*;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request.RealizarEmprestimoRequest;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.EmprestimoAtrasadoResponse;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.EmprestimoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/v1/emprestimos")
public class EmprestimoController {
    private final RealizarEmprestimoUseCase realizarEmprestimoUseCase;
    private final DevolverLivroUseCase devolverLivroUseCase;
    private final ListarAtrasadosUseCase listarAtrasadosUseCase;
    private final RenovarEmprestimoUseCase renovarEmprestimoUseCase;
    private final ConsultarHistoricoLeitorUseCase consultarHistoricoLeitorUseCase;
    private final ClockPort clockPort;
    public EmprestimoController(RealizarEmprestimoUseCase realizarEmprestimoUseCase, DevolverLivroUseCase devolverLivroUseCase, ListarAtrasadosUseCase listarAtrasadosUseCase, RenovarEmprestimoUseCase renovarEmprestimoUseCase, ConsultarHistoricoLeitorUseCase consultarHistoricoLeitorUseCase, ClockPort clockPort) {
        this.realizarEmprestimoUseCase = realizarEmprestimoUseCase; this.devolverLivroUseCase = devolverLivroUseCase;
        this.listarAtrasadosUseCase = listarAtrasadosUseCase; this.renovarEmprestimoUseCase = renovarEmprestimoUseCase;
        this.consultarHistoricoLeitorUseCase = consultarHistoricoLeitorUseCase; this.clockPort = clockPort;
    }
    @PostMapping
    public ResponseEntity<EmprestimoResponse> emprestar(@Valid @RequestBody RealizarEmprestimoRequest request) {
        Emprestimo emp = realizarEmprestimoUseCase.executar(new RealizarEmprestimoUseCase.Comando(request.livroId(), request.leitorId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(EmprestimoResponse.de(emp));
    }
    @PostMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoResponse> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(EmprestimoResponse.de(devolverLivroUseCase.executar(id)));
    }
    @GetMapping("/atrasados")
    public ResponseEntity<List<EmprestimoAtrasadoResponse>> listarAtrasados() {
        LocalDate hoje = clockPort.dataAtual();
        return ResponseEntity.ok(listarAtrasadosUseCase.executar().stream().map(e -> EmprestimoAtrasadoResponse.de(e, hoje)).toList());
    }
    @PostMapping("/{id}/renovacao")
    public ResponseEntity<EmprestimoResponse> renovar(@PathVariable Long id) {
        return ResponseEntity.ok(EmprestimoResponse.de(renovarEmprestimoUseCase.executar(id)));
    }
    @GetMapping("/leitor/{leitorId}")
    public ResponseEntity<List<EmprestimoResponse>> historicoPorLeitor(@PathVariable Long leitorId) {
        return ResponseEntity.ok(consultarHistoricoLeitorUseCase.executar(leitorId).stream().map(EmprestimoResponse::de).toList());
    }
}