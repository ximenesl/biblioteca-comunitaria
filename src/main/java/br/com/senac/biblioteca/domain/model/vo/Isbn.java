package br.com.senac.biblioteca.domain.model.vo;
import br.com.senac.biblioteca.domain.exception.ValidationException;
import java.util.regex.Pattern;
public record Isbn(String valor) {
    private static final Pattern ISBN_PATTERN = Pattern.compile("^[0-9X-]{10,17}$");
    public Isbn {
        if (valor == null || valor.isBlank()) throw new ValidationException("O ISBN e obrigatorio.");
        valor = valor.trim().toUpperCase();
        if (!ISBN_PATTERN.matcher(valor).matches()) throw new ValidationException("Formato de ISBN invalido.");
    }
    public static Isbn de(String valor) { return new Isbn(valor); }
    @Override public String toString() { return valor; }
}