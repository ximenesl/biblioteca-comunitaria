package br.com.senac.biblioteca.infrastructure.adapters.in.web.controller;

import br.com.senac.biblioteca.domain.ports.in.ListarAtrasadosUseCase;
import br.com.senac.biblioteca.domain.ports.in.ObterRankingLivrosUseCase;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.EmprestimoAtrasadoResponse;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.RankingLivroResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/relatorios")
public class RelatorioController {

    private final ObterRankingLivrosUseCase obterRankingLivrosUseCase;
    private final ListarAtrasadosUseCase listarAtrasadosUseCase;
    private final ClockPort clockPort;

    public RelatorioController(
            ObterRankingLivrosUseCase obterRankingLivrosUseCase,
            ListarAtrasadosUseCase listarAtrasadosUseCase,
            ClockPort clockPort) {
        this.obterRankingLivrosUseCase = obterRankingLivrosUseCase;
        this.listarAtrasadosUseCase = listarAtrasadosUseCase;
        this.clockPort = clockPort;
    }

    @GetMapping("/ranking-livros")
    public ResponseEntity<List<RankingLivroResponse>> rankingLivros() {
        return ResponseEntity.ok(obterRankingLivrosUseCase.executar().stream().map(RankingLivroResponse::de).toList());
    }

    @GetMapping("/livros-mais-emprestados")
    public ResponseEntity<List<RankingLivroResponse>> livrosMaisEmprestados() {
        return rankingLivros();
    }

    @GetMapping("/atrasados")
    public ResponseEntity<List<EmprestimoAtrasadoResponse>> listarAtrasados() {
        LocalDate hoje = clockPort.dataAtual();
        return ResponseEntity.ok(listarAtrasadosUseCase.executar().stream().map(e -> EmprestimoAtrasadoResponse.de(e, hoje)).toList());
    }
}
