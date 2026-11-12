package br.com.senac.biblioteca.infrastructure.configuration;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.*;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
@Component
public class DataInitializer implements CommandLineRunner {
    private final SpringDataLeitorRepository leitorRepository;
    private final SpringDataLivroRepository livroRepository;
    private final SpringDataEmprestimoRepository emprestimoRepository;
    public DataInitializer(SpringDataLeitorRepository leitorRepository, SpringDataLivroRepository livroRepository, SpringDataEmprestimoRepository emprestimoRepository) {
        this.leitorRepository = leitorRepository; this.livroRepository = livroRepository; this.emprestimoRepository = emprestimoRepository;
    }
    @Override public void run(String... args) {
        if (leitorRepository.count() > 0) return;
        LeitorJpaEntity leitor1 = leitorRepository.save(new LeitorJpaEntity(null, "Ana Clara Souza"));
        LeitorJpaEntity leitor2 = leitorRepository.save(new LeitorJpaEntity(null, "Bruno Albuquerque"));
        leitorRepository.save(new LeitorJpaEntity(null, "Carlos Eduardo Lima"));
        leitorRepository.save(new LeitorJpaEntity(null, "Daniela Martins"));
        leitorRepository.save(new LeitorJpaEntity(null, "Eduardo Gomes"));

        LivroJpaEntity livro1 = livroRepository.save(new LivroJpaEntity(null, "Clean Code", "Robert C. Martin", "978-0132350884", 2));
        livroRepository.save(new LivroJpaEntity(null, "Domain-Driven Design", "Eric Evans", "978-0321125217", 1));
        livroRepository.save(new LivroJpaEntity(null, "Refactoring", "Martin Fowler", "978-0134757599", 1));
        LivroJpaEntity livro4 = livroRepository.save(new LivroJpaEntity(null, "O Pequeno Principe", "Antoine de Saint-Exupery", "978-8522031405", 3));
        livroRepository.save(new LivroJpaEntity(null, "O Senhor dos Anéis: A Sociedade do Anel", "J. R. R. Tolkien", "978-8595084759", 3));

        // Emprestimo antigo em atraso (CA4)
        LocalDate hoje = LocalDate.now();
        emprestimoRepository.save(new EmprestimoJpaEntity(null, livro1, leitor1, hoje.minusDays(20), hoje.minusDays(6), null, 0));
        // Emprestimo ja devolvido (nao deve aparecer no CA4)
        emprestimoRepository.save(new EmprestimoJpaEntity(null, livro4, leitor2, hoje.minusDays(30), hoje.minusDays(16), hoje.minusDays(17), 0));
    }
}