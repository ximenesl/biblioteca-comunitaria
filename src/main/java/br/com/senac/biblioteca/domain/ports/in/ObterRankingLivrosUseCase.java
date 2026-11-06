package br.com.senac.biblioteca.domain.ports.in;
import br.com.senac.biblioteca.domain.model.RankingLivro;
import java.util.List;
public interface ObterRankingLivrosUseCase {
    List<RankingLivro> executar();
}