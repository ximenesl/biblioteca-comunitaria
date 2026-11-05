package br.com.senac.biblioteca.infrastructure.adapters.in.web.controller;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.LivroComDisponibilidade;
import br.com.senac.biblioteca.domain.ports.in.BuscarLivrosUseCase;
import br.com.senac.biblioteca.domain.ports.in.CadastrarLivroUseCase;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request.CadastrarLivroRequest;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.LivroResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/livros")
public class LivroController {
    private final CadastrarLivroUseCase cadastrarLivroUseCase;
    private final BuscarLivrosUseCase buscarLivrosUseCase;
    public LivroController(CadastrarLivroUseCase cadastrarLivroUseCase, BuscarLivrosUseCase buscarLivrosUseCase) {
        this.cadastrarLivroUseCase = cadastrarLivroUseCase; this.buscarLivrosUseCase = buscarLivrosUseCase;
    }
    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(@Valid @RequestBody CadastrarLivroRequest request) {
        Livro salvo = cadastrarLivroUseCase.executar(new CadastrarLivroUseCase.Comando(request.titulo(), request.autor(), request.isbn(), request.exemplares()));
        return ResponseEntity.status(HttpStatus.CREATED).body(LivroResponse.de(salvo, salvo.getExemplares()));
    }
    @GetMapping
    public ResponseEntity<List<LivroResponse>> buscar(@RequestParam(required = false) String busca) {
        List<LivroComDisponibilidade> livros = buscarLivrosUseCase.executar(busca);
        return ResponseEntity.ok(livros.stream().map(LivroResponse::de).toList());
    }
}