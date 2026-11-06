package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de uma nova obra no acervo da biblioteca")
public record CadastrarLivroRequest(
        @Schema(description = "Título completo do livro", example = "O Senhor dos Anéis: A Sociedade do Anel")
        @NotBlank @Size(max = 200)
        String titulo,

        @Schema(description = "Nome do autor ou autores da obra", example = "J. R. R. Tolkien")
        @NotBlank @Size(max = 150)
        String autor,

        @Schema(description = "ISBN canônico único (10 ou 13 dígitos numéricos com hífens opcionais)", example = "978-8595084759")
        @NotBlank @Pattern(regexp = "^[0-9X-]{10,17}$")
        String isbn,

        @Schema(description = "Quantidade de cópias físicas adquiridas pelo acervo (mínimo 1)", example = "3")
        @NotNull @Min(1)
        Integer exemplares
) {}
