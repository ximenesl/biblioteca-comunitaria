package br.com.senac.biblioteca.domain.model;
public record RankingLivro(Long livroId, String titulo, String autor, long totalEmprestimos) {}