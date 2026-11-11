package br.com.senac.biblioteca.infrastructure.adapters.in.web.controller;

import br.com.senac.biblioteca.domain.ports.in.ListarAtrasadosUseCase;
import br.com.senac.biblioteca.domain.ports.in.ObterRankingLivrosUseCase;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.EmprestimoAtrasadoResponse;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.RankingLivroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/relatorios")
@Tag(name = "Relatórios", description = "Relatórios analíticos sobre o acervo e empréstimos")
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
    @Operation(
        summary = "Ranking de livros mais emprestados",
        description = "Retorna a lista de livros ordenada pelo número total de empréstimos realizados.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ranking gerado com sucesso")
        }
    )
    public ResponseEntity<List<RankingLivroResponse>> rankingLivros() {
        return ResponseEntity.ok(obterRankingLivrosUseCase.executar().stream().map(RankingLivroResponse::de).toList());
    }

    @GetMapping("/livros-mais-emprestados")
    @Operation(
        summary = "Livros mais emprestados (alias)",
        description = "Alias de GET /ranking-livros — retorna o mesmo ranking de livros mais emprestados.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ranking gerado com sucesso")
        }
    )
    public ResponseEntity<List<RankingLivroResponse>> livrosMaisEmprestados() {
        return rankingLivros();
    }

    @GetMapping("/atrasados")
    @Operation(
        summary = "Relatório de empréstimos atrasados",
        description = "Lista todos os empréstimos vencidos com os dias de atraso calculados em relação à data atual.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
        }
    )
    public ResponseEntity<List<EmprestimoAtrasadoResponse>> listarAtrasados() {
        LocalDate hoje = clockPort.dataAtual();
        return ResponseEntity.ok(listarAtrasadosUseCase.executar().stream()
                .map(e -> EmprestimoAtrasadoResponse.de(e, hoje)).toList());
    }
}
