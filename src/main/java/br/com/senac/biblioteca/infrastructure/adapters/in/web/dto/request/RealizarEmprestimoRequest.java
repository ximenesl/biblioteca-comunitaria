package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request;
import jakarta.validation.constraints.NotNull;
public record RealizarEmprestimoRequest(@NotNull Long livroId, @NotNull Long leitorId) {}