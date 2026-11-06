package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.LivroComDisponibilidade;
public record LivroResponse(Long id, String titulo, String autor, String isbn, int exemplares, int disponiveis) {
    public static LivroResponse de(Livro livro, int disponiveis) {
        return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn().valor(), livro.getExemplares(), disponiveis);
    }
    public static LivroResponse de(LivroComDisponibilidade l) {
        return new LivroResponse(l.id(), l.titulo(), l.autor(), l.isbn(), l.exemplares(), l.disponiveis());
    }
}