package br.com.senac.biblioteca.infrastructure.adapters.in.web.controller;

import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.LivroComDisponibilidade;
import br.com.senac.biblioteca.domain.ports.in.BuscarLivrosUseCase;
import br.com.senac.biblioteca.domain.ports.in.CadastrarLivroUseCase;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request.CadastrarLivroRequest;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.LivroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/livros")
@Tag(name = "Livros", description = "Gestão do acervo bibliográfico — cadastro e consulta de obras")
public class LivroController {

    private final CadastrarLivroUseCase cadastrarLivroUseCase;
    private final BuscarLivrosUseCase buscarLivrosUseCase;

    public LivroController(CadastrarLivroUseCase cadastrarLivroUseCase, BuscarLivrosUseCase buscarLivrosUseCase) {
        this.cadastrarLivroUseCase = cadastrarLivroUseCase;
        this.buscarLivrosUseCase = buscarLivrosUseCase;
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar novo livro",
        description = "Adiciona uma nova obra ao acervo da biblioteca com suas cópias físicas.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CadastrarLivroRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Livro de Fantasia",
                        summary = "O Senhor dos Anéis",
                        value = """
                            {
                              "titulo": "O Senhor dos Anéis: A Sociedade do Anel",
                              "autor": "J. R. R. Tolkien",
                              "isbn": "978-8595084759",
                              "exemplares": 3
                            }"""
                    ),
                    @ExampleObject(
                        name = "Livro Técnico",
                        summary = "Clean Code",
                        value = """
                            {
                              "titulo": "Clean Code: A Handbook of Agile Software Craftsmanship",
                              "autor": "Robert C. Martin",
                              "isbn": "978-0132350884",
                              "exemplares": 2
                            }"""
                    ),
                    @ExampleObject(
                        name = "Livro Nacional",
                        summary = "Dom Casmurro",
                        value = """
                            {
                              "titulo": "Dom Casmurro",
                              "autor": "Machado de Assis",
                              "isbn": "978-8520914839",
                              "exemplares": 5
                            }"""
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Livro cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (campos obrigatórios ausentes ou formato incorreto)", content = @Content),
            @ApiResponse(responseCode = "409", description = "ISBN já cadastrado no acervo", content = @Content)
        }
    )
    public ResponseEntity<LivroResponse> cadastrar(@Valid @RequestBody CadastrarLivroRequest request) {
        Livro salvo = cadastrarLivroUseCase.executar(new CadastrarLivroUseCase.Comando(
            request.titulo(),
            request.autor(),
            request.isbn(),
            request.exemplares()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(LivroResponse.de(salvo, salvo.getExemplares()));
    }

    @GetMapping
    @Operation(
        summary = "Listar / buscar livros do acervo",
        description = "Retorna todos os livros cadastrados. Use o parâmetro `busca` para filtrar por título, autor ou ISBN.",
        parameters = @Parameter(
            name = "busca",
            description = "Texto para filtrar por título, autor ou ISBN (opcional)",
            example = "Tolkien"
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso")
        }
    )
    public ResponseEntity<List<LivroResponse>> listar(@RequestParam(required = false) String busca) {
        List<LivroComDisponibilidade> livros = buscarLivrosUseCase.executar(busca);
        return ResponseEntity.ok(livros.stream().map(LivroResponse::de).toList());
    }

    @GetMapping("/busca")
    @Operation(
        summary = "Buscar livros por termo",
        description = "Alias de GET /api/v1/livros com parâmetro `busca` obrigatório.",
        parameters = @Parameter(name = "busca", description = "Texto de busca por título, autor ou ISBN", example = "Clean Code"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Resultado da busca")
        }
    )
    public ResponseEntity<List<LivroResponse>> buscar(@RequestParam(required = false) String busca) {
        return listar(busca);
    }
}
