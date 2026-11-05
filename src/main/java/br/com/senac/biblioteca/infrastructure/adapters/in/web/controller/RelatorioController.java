package br.com.senac.biblioteca.infrastructure.adapters.in.web.controller;
import br.com.senac.biblioteca.domain.ports.in.ObterRankingLivrosUseCase;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.RankingLivroResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/api/v1/relatorios")
public class RelatorioController {
    private final ObterRankingLivrosUseCase obterRankingLivrosUseCase;
    public RelatorioController(ObterRankingLivrosUseCase obterRankingLivrosUseCase) { this.obterRankingLivrosUseCase = obterRankingLivrosUseCase; }
    @GetMapping("/ranking-livros")
    public ResponseEntity<List<RankingLivroResponse>> rankingLivros() {
        return ResponseEntity.ok(obterRankingLivrosUseCase.executar().stream().map(RankingLivroResponse::de).toList());
    }
}