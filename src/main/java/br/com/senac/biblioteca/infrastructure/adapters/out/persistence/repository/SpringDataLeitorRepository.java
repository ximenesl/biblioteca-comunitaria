package br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.LeitorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SpringDataLeitorRepository extends JpaRepository<LeitorJpaEntity, Long> {}