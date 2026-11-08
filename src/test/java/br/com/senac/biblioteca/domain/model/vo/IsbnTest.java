package br.com.senac.biblioteca.domain.model.vo;

import br.com.senac.biblioteca.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IsbnTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "978-85-7522-563-9",
            "9788575225639",
            "85-7522-563-X",
            "857522563X",
            "0-13-235088-2",
            "0132350882"
    })
    @DisplayName("Deve aceitar e normalizar ISBN válido em formato de 10 ou 13 dígitos")
    void deveCriarIsbnValido(String valor) {
        Isbn isbn = new Isbn(valor);
        assertNotNull(isbn.valor());
        assertFalse(isbn.valor().isBlank());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "   ",
            "123",
            "isbn-invalido",
            "978-85-7522-563-9999",
            "978@8575225639"
    })
    @DisplayName("Deve lançar ValidationException para formatos de ISBN inválidos")
    void deveRejeitarIsbnInvalido(String valor) {
        assertThrows(ValidationException.class, () -> new Isbn(valor));
    }

    @Test
    @DisplayName("Deve rejeitar valor nulo de ISBN")
    void deveRejeitarIsbnNulo() {
        assertThrows(ValidationException.class, () -> new Isbn(null));
    }

    @Test
    @DisplayName("Dois ISBNs com pontuações equivalentes devem ser iguais")
    void deveGarantirIgualdadePorValor() {
        Isbn isbn1 = new Isbn("978-85-7522-563-9");
        Isbn isbn2 = new Isbn("978-85-7522-563-9");
        assertEquals(isbn1, isbn2);
        assertEquals(isbn1.hashCode(), isbn2.hashCode());
    }
}

