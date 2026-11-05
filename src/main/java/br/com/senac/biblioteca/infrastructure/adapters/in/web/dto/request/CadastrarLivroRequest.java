package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
public record CadastrarLivroRequest(
        @NotBlank @Size(max = 200) String titulo,
        @NotBlank @Size(max = 150) String autor,
        @NotBlank @Pattern(regexp = "^[0-9X-]{10,17}$") String isbn,
        @NotNull @Min(1) Integer exemplares
) {}