package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response;

import br.com.senac.biblioteca.domain.model.RankingLivro;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Posição analítica no ranking de livros mais procurados")
public record RankingLivroResponse(
        @Schema(description = "Identificador do livro", example = "1")
        Long livroId,

        @Schema(description = "Título da obra", example = "Clean Code")
        String titulo,

        @Schema(description = "Nome do autor", example = "Robert C. Martin")
        String autor,

        @Schema(description = "Quantidade total histórica de empréstimos concedidos", example = "15")
        long totalEmprestimos
) {
    public static RankingLivroResponse de(RankingLivro r) {
        return new RankingLivroResponse(r.livroId(), r.titulo(), r.autor(), r.totalEmprestimos());
    }
}
