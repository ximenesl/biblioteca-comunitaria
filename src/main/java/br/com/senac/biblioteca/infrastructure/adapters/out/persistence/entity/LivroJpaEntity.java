package br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;
@Entity
@Table(name = "livros", uniqueConstraints = {@UniqueConstraint(name = "uk_livro_isbn", columnNames = "isbn")})
@Check(constraints = "exemplares >= 1")
public class LivroJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200) private String titulo;
    @Column(nullable = false, length = 150) private String autor;
    @Column(nullable = false, unique = true, length = 20) private String isbn;
    @Column(nullable = false) private int exemplares;

    public LivroJpaEntity() {}
    public LivroJpaEntity(Long id, String titulo, String autor, String isbn, int exemplares) {
        this.id = id; this.titulo = titulo; this.autor = autor; this.isbn = isbn; this.exemplares = exemplares;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getExemplares() { return exemplares; }
    public void setExemplares(int exemplares) { this.exemplares = exemplares; }
}