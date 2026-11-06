package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response;

import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.LivroComDisponibilidade;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação detalhada de um livro do acervo e seu estoque atual")
public record LivroResponse(
        @Schema(description = "Identificador único do livro", example = "1")
        Long id,

        @Schema(description = "Título da obra", example = "O Senhor dos Anéis: A Sociedade do Anel")
        String titulo,

        @Schema(description = "Autor da obra", example = "J. R. R. Tolkien")
        String autor,

        @Schema(description = "ISBN canônico", example = "978-8595084759")
        String isbn,

        @Schema(description = "Total de cópias físicas cadastradas", example = "3")
        int exemplares,

        @Schema(description = "Quantidade de cópias disponíveis para empréstimo imediato", example = "2")
        int disponiveis
) {
    public static LivroResponse de(Livro livro, int disponiveis) {
        return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn().valor(), livro.getExemplares(), disponiveis);
    }

    public static LivroResponse de(LivroComDisponibilidade l) {
        return new LivroResponse(l.id(), l.titulo(), l.autor(), l.isbn(), l.exemplares(), l.disponiveis());
    }
}
