package br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository;
import br.com.senac.biblioteca.domain.model.RankingLivro;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.EmprestimoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface SpringDataEmprestimoRepository extends JpaRepository<EmprestimoJpaEntity, Long> {
    @Query("SELECT COUNT(e) FROM EmprestimoJpaEntity e WHERE e.leitor.id = :leitorId AND e.devolvidoEm IS NULL")
    int contarAtivosPorLeitor(@Param("leitorId") Long leitorId);

    @Query("SELECT COUNT(e) FROM EmprestimoJpaEntity e WHERE e.livro.id = :livroId AND e.devolvidoEm IS NULL")
    int contarAtivosPorLivro(@Param("livroId") Long livroId);

    @Query("SELECT e FROM EmprestimoJpaEntity e JOIN FETCH e.livro JOIN FETCH e.leitor WHERE e.devolvidoEm IS NULL AND e.previstoPara < :dataReferencia ORDER BY e.previstoPara ASC")
    List<EmprestimoJpaEntity> buscarAtrasados(@Param("dataReferencia") LocalDate dataReferencia);

    @Query("SELECT e FROM EmprestimoJpaEntity e JOIN FETCH e.livro JOIN FETCH e.leitor WHERE e.leitor.id = :leitorId ORDER BY e.emprestadoEm DESC")
    List<EmprestimoJpaEntity> buscarPorLeitor(@Param("leitorId") Long leitorId);

    @Query("SELECT new br.com.senac.biblioteca.domain.model.RankingLivro(e.livro.id, e.livro.titulo, e.livro.autor, COUNT(e.id)) FROM EmprestimoJpaEntity e GROUP BY e.livro.id, e.livro.titulo, e.livro.autor ORDER BY COUNT(e.id) DESC")
    List<RankingLivro> obterRankingMaisEmprestados();
}