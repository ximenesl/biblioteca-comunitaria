package br.com.senac.biblioteca.application.usecase;
import br.com.senac.biblioteca.domain.exception.BusinessConflictException;
import br.com.senac.biblioteca.domain.model.Livro;
import br.com.senac.biblioteca.domain.model.vo.Isbn;
import br.com.senac.biblioteca.domain.ports.in.CadastrarLivroUseCase;
import br.com.senac.biblioteca.domain.ports.out.LivroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CadastrarLivroService implements CadastrarLivroUseCase {
    private final LivroRepositoryPort livroRepositoryPort;
    public CadastrarLivroService(LivroRepositoryPort livroRepositoryPort) { this.livroRepositoryPort = livroRepositoryPort; }
    @Override @Transactional
    public Livro executar(Comando comando) {
        Isbn isbn = Isbn.de(comando.isbn());
        if (livroRepositoryPort.existePorIsbn(isbn)) {
            throw new BusinessConflictException("Ja existe um livro cadastrado com o ISBN informado: " + isbn.valor());
        }
        return livroRepositoryPort.salvar(Livro.novo(comando.titulo(), comando.autor(), comando.isbn(), comando.exemplares()));
    }
}