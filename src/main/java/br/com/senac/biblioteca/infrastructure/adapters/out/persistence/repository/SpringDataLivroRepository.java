package br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.LivroJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface SpringDataLivroRepository extends JpaRepository<LivroJpaEntity, Long> {
    Optional<LivroJpaEntity> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    @Query("SELECT l FROM LivroJpaEntity l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<LivroJpaEntity> buscarPorTermo(@Param("termo") String termo);
}