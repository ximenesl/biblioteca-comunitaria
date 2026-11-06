package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response;

import br.com.senac.biblioteca.domain.model.Emprestimo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Schema(description = "Registro de empréstimo em situação de atraso")
public record EmprestimoAtrasadoResponse(
        @Schema(description = "Identificador do empréstimo", example = "1")
        Long id,

        @Schema(description = "Identificador do livro", example = "1")
        Long livroId,

        @Schema(description = "Título da obra em atraso", example = "Clean Code")
        String livroTitulo,

        @Schema(description = "Identificador do leitor em débito", example = "1")
        Long leitorId,

        @Schema(description = "Nome do leitor", example = "Ana Clara Souza")
        String leitorNome,

        @Schema(description = "Data em que foi emprestado", example = "2026-10-17")
        LocalDate emprestadoEm,

        @Schema(description = "Data prevista original de devolução", example = "2026-10-31")
        LocalDate previstoPara,

        @Schema(description = "Total de dias decorridos após o vencimento do prazo", example = "6")
        long diasAtraso
) {
    public static EmprestimoAtrasadoResponse de(Emprestimo domain, LocalDate hoje) {
        long dias = ChronoUnit.DAYS.between(domain.getPrevistoPara(), hoje);
        return new EmprestimoAtrasadoResponse(domain.getId(), domain.getLivro().getId(), domain.getLivro().getTitulo(), domain.getLeitor().getId(), domain.getLeitor().getNome(), domain.getEmprestadoEm(), domain.getPrevistoPara(), Math.max(0, dias));
    }
}
