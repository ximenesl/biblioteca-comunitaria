package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import java.time.LocalDate;
public record EmprestimoResponse(Long id, Long livroId, String livroTitulo, Long leitorId, String leitorNome, LocalDate emprestadoEm, LocalDate previstoPara, LocalDate devolvidoEm, String status, int renovacoes) {
    public static EmprestimoResponse de(Emprestimo domain) {
        String status = domain.isDevolvido() ? "DEVOLVIDO" : "ATIVO";
        return new EmprestimoResponse(domain.getId(), domain.getLivro().getId(), domain.getLivro().getTitulo(), domain.getLeitor().getId(), domain.getLeitor().getNome(), domain.getEmprestadoEm(), domain.getPrevistoPara(), domain.getDevolvidoEm(), status, domain.getQuantidadeRenovacoes());
    }
}