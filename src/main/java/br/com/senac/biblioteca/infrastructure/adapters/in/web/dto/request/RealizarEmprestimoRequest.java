package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para realização de empréstimo de um livro para um leitor")
public record RealizarEmprestimoRequest(
        @Schema(description = "ID do livro a ser emprestado", example = "1")
        @NotNull
        Long livroId,

        @Schema(description = "ID do leitor que está retirando o livro", example = "1")
        @NotNull
        Long leitorId
) {}
