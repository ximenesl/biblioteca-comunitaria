package br.com.senac.biblioteca.infrastructure.adapters.out.persistence.mapper;
import br.com.senac.biblioteca.domain.model.*;
import br.com.senac.biblioteca.domain.model.vo.Isbn;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.*;
import org.springframework.stereotype.Component;
@Component
public class BibliotecaPersistenceMapper {
    public Livro toDomain(LivroJpaEntity entity) {
        if (entity == null) return null;
        return new Livro(entity.getId(), entity.getTitulo(), entity.getAutor(), Isbn.de(entity.getIsbn()), entity.getExemplares());
    }
    public LivroJpaEntity toJpaEntity(Livro domain) {
        if (domain == null) return null;
        return new LivroJpaEntity(domain.getId(), domain.getTitulo(), domain.getAutor(), domain.getIsbn().valor(), domain.getExemplares());
    }
    public Leitor toDomain(LeitorJpaEntity entity) {
        if (entity == null) return null;
        return new Leitor(entity.getId(), entity.getNome());
    }
    public LeitorJpaEntity toJpaEntity(Leitor domain) {
        if (domain == null) return null;
        return new LeitorJpaEntity(domain.getId(), domain.getNome());
    }
    public Emprestimo toDomain(EmprestimoJpaEntity entity) {
        if (entity == null) return null;
        return new Emprestimo(entity.getId(), toDomain(entity.getLivro()), toDomain(entity.getLeitor()), entity.getEmprestadoEm(), entity.getPrevistoPara(), entity.getDevolvidoEm(), entity.getQuantidadeRenovacoes());
    }
    public EmprestimoJpaEntity toJpaEntity(Emprestimo domain) {
        if (domain == null) return null;
        return new EmprestimoJpaEntity(domain.getId(), toJpaEntity(domain.getLivro()), toJpaEntity(domain.getLeitor()), domain.getEmprestadoEm(), domain.getPrevistoPara(), domain.getDevolvidoEm(), domain.getQuantidadeRenovacoes());
    }
}