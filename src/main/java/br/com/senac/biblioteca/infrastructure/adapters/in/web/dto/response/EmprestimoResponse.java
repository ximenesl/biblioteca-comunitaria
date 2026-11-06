package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response;

import br.com.senac.biblioteca.domain.model.Emprestimo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados detalhados do registro de empréstimo")
public record EmprestimoResponse(
        @Schema(description = "Identificador único do empréstimo", example = "10")
        Long id,

        @Schema(description = "Identificador do livro", example = "1")
        Long livroId,

        @Schema(description = "Título do livro emprestado", example = "Clean Code")
        String livroTitulo,

        @Schema(description = "Identificador do leitor", example = "1")
        Long leitorId,

        @Schema(description = "Nome do leitor", example = "Ana Clara Souza")
        String leitorNome,

        @Schema(description = "Data em que o empréstimo foi realizado", example = "2026-11-06")
        LocalDate emprestadoEm,

        @Schema(description = "Data limite prevista para devolução (data + 14 dias)", example = "2026-11-20")
        LocalDate previstoPara,

        @Schema(description = "Data efetiva em que o livro foi devolvido (nulo se ativo)", example = "null")
        LocalDate devolvidoEm,

        @Schema(description = "Situação atual do empréstimo", example = "ATIVO")
        String status,

        @Schema(description = "Quantidade de renovações de prazo concedidas", example = "0")
        int renovacoes
) {
    public static EmprestimoResponse de(Emprestimo domain) {
        String status = domain.isDevolvido() ? "DEVOLVIDO" : "ATIVO";
        return new EmprestimoResponse(domain.getId(), domain.getLivro().getId(), domain.getLivro().getTitulo(), domain.getLeitor().getId(), domain.getLeitor().getNome(), domain.getEmprestadoEm(), domain.getPrevistoPara(), domain.getDevolvidoEm(), status, domain.getQuantidadeRenovacoes());
    }
}
