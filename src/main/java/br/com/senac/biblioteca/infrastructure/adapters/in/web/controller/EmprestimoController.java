package br.com.senac.biblioteca.infrastructure.adapters.in.web.controller;

import br.com.senac.biblioteca.domain.model.Emprestimo;
import br.com.senac.biblioteca.domain.ports.in.*;
import br.com.senac.biblioteca.domain.ports.out.ClockPort;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request.RealizarEmprestimoRequest;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.EmprestimoAtrasadoResponse;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response.EmprestimoResponse;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/emprestimos")
@Tag(name = "Empréstimos", description = "Realização, devolução, renovação e consulta de empréstimos de livros")
public class EmprestimoController {

    private final RealizarEmprestimoUseCase realizarEmprestimoUseCase;
    private final DevolverLivroUseCase devolverLivroUseCase;
    private final ListarAtrasadosUseCase listarAtrasadosUseCase;
    private final RenovarEmprestimoUseCase renovarEmprestimoUseCase;
    private final ConsultarHistoricoLeitorUseCase consultarHistoricoLeitorUseCase;
    private final ClockPort clockPort;

    public EmprestimoController(RealizarEmprestimoUseCase realizarEmprestimoUseCase,
                                 DevolverLivroUseCase devolverLivroUseCase,
                                 ListarAtrasadosUseCase listarAtrasadosUseCase,
                                 RenovarEmprestimoUseCase renovarEmprestimoUseCase,
                                 ConsultarHistoricoLeitorUseCase consultarHistoricoLeitorUseCase,
                                 ClockPort clockPort) {
        this.realizarEmprestimoUseCase = realizarEmprestimoUseCase;
        this.devolverLivroUseCase = devolverLivroUseCase;
        this.listarAtrasadosUseCase = listarAtrasadosUseCase;
        this.renovarEmprestimoUseCase = renovarEmprestimoUseCase;
        this.consultarHistoricoLeitorUseCase = consultarHistoricoLeitorUseCase;
        this.clockPort = clockPort;
    }

    @PostMapping
    @Operation(
        summary = "Realizar empréstimo",
        description = "Registra a retirada de um livro por um leitor. O prazo de devolução é calculado automaticamente (14 dias). O leitor não pode ter mais de 3 empréstimos ativos.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RealizarEmprestimoRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Empréstimo básico",
                        summary = "Leitor 1 retira o livro 1",
                        value = """
                            {
                              "livroId": 1,
                              "leitorId": 1
                            }"""
                    ),
                    @ExampleObject(
                        name = "Segundo empréstimo",
                        summary = "Leitor 2 retira o livro 2",
                        value = """
                            {
                              "livroId": 2,
                              "leitorId": 2
                            }"""
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Empréstimo registrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro ou leitor não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Livro sem cópias disponíveis ou leitor atingiu limite de empréstimos", content = @Content)
        }
    )
    public ResponseEntity<EmprestimoResponse> emprestar(@Valid @RequestBody RealizarEmprestimoRequest request) {
        Emprestimo emp = realizarEmprestimoUseCase.executar(new RealizarEmprestimoUseCase.Comando(request.livroId(), request.leitorId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(EmprestimoResponse.de(emp));
    }

    @PostMapping("/{id}/devolucao")
    @Operation(
        summary = "Devolver livro",
        description = "Registra a devolução de um empréstimo ativo, liberando a cópia para novos empréstimos.",
        parameters = @Parameter(name = "id", description = "ID do empréstimo a ser devolvido", example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Devolução registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Empréstimo já devolvido", content = @Content)
        }
    )
    public ResponseEntity<EmprestimoResponse> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(EmprestimoResponse.de(devolverLivroUseCase.executar(id)));
    }

    @GetMapping("/atrasados")
    @Operation(
        summary = "Listar empréstimos em atraso",
        description = "Retorna todos os empréstimos cujo prazo de devolução já expirou.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos atrasados")
        }
    )
    public ResponseEntity<List<EmprestimoAtrasadoResponse>> listarAtrasados() {
        LocalDate hoje = clockPort.dataAtual();
        return ResponseEntity.ok(listarAtrasadosUseCase.executar().stream()
                .map(e -> EmprestimoAtrasadoResponse.de(e, hoje)).toList());
    }

    @PostMapping("/{id}/renovacao")
    @Operation(
        summary = "Renovar empréstimo",
        description = "Estende o prazo de devolução em 7 dias. Máximo de 1 renovação por empréstimo.",
        parameters = @Parameter(name = "id", description = "ID do empréstimo a ser renovado", example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Renovação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Empréstimo já renovado ou devolvido", content = @Content)
        }
    )
    public ResponseEntity<EmprestimoResponse> renovar(@PathVariable Long id) {
        return ResponseEntity.ok(EmprestimoResponse.de(renovarEmprestimoUseCase.executar(id)));
    }

    @GetMapping("/leitor/{leitorId}")
    @Operation(
        summary = "Histórico de empréstimos por leitor",
        description = "Retorna todos os empréstimos (ativos e devolvidos) associados a um leitor.",
        parameters = @Parameter(name = "leitorId", description = "ID do leitor", example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Leitor não encontrado", content = @Content)
        }
    )
    public ResponseEntity<List<EmprestimoResponse>> historicoPorLeitor(@PathVariable Long leitorId) {
        return ResponseEntity.ok(consultarHistoricoLeitorUseCase.executar(leitorId).stream()
                .map(EmprestimoResponse::de).toList());
    }
}