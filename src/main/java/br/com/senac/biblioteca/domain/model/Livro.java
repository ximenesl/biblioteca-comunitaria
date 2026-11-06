package br.com.senac.biblioteca.domain.model;
import br.com.senac.biblioteca.domain.exception.ValidationException;
import br.com.senac.biblioteca.domain.model.vo.Isbn;
import java.util.Objects;
public class Livro {
    private final Long id;
    private final String titulo;
    private final String autor;
    private final Isbn isbn;
    private final int exemplares;

    public Livro(Long id, String titulo, String autor, Isbn isbn, int exemplares) {
        if (titulo == null || titulo.isBlank()) throw new ValidationException("O titulo e obrigatorio.");
        if (autor == null || autor.isBlank()) throw new ValidationException("O autor e obrigatorio.");
        if (isbn == null) throw new ValidationException("O ISBN e obrigatorio.");
        if (exemplares < 1) throw new ValidationException("Exemplares deve ser >= 1.");
        this.id = id;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.isbn = isbn;
        this.exemplares = exemplares;
    }
    public static Livro novo(String titulo, String autor, String isbnValor, int exemplares) {
        return new Livro(null, titulo, autor, Isbn.de(isbnValor), exemplares);
    }
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public Isbn getIsbn() { return isbn; }
    public int getExemplares() { return exemplares; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro livro)) return false;
        return Objects.equals(isbn, livro.isbn);
    }
    @Override public int hashCode() { return Objects.hashCode(isbn); }
}