package br.com.senac.biblioteca.domain.model;
public record LivroComDisponibilidade(Long id, String titulo, String autor, String isbn, int exemplares, int disponiveis) {}