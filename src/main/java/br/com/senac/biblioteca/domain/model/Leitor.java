package br.com.senac.biblioteca.domain.model;
import br.com.senac.biblioteca.domain.exception.ValidationException;
import java.util.Objects;
public class Leitor {
    private final Long id;
    private final String nome;
    public Leitor(Long id, String nome) {
        if (nome == null || nome.isBlank()) throw new ValidationException("O nome e obrigatorio.");
        this.id = id;
        this.nome = nome.trim();
    }
    public static Leitor novo(String nome) { return new Leitor(null, nome); }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leitor leitor)) return false;
        return Objects.equals(id, leitor.id);
    }
    @Override public int hashCode() { return Objects.hashCode(id); }
}