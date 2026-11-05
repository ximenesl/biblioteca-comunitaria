package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response;
import br.com.senac.biblioteca.domain.model.RankingLivro;
public record RankingLivroResponse(Long livroId, String titulo, String autor, long totalEmprestimos) {
    public static RankingLivroResponse de(RankingLivro domain) {
        return new RankingLivroResponse(domain.livroId(), domain.titulo(), domain.autor(), domain.totalEmprestimos());
    }
}