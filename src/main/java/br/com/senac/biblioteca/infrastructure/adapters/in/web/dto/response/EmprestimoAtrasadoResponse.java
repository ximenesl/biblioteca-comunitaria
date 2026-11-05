package br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.response;
import br.com.senac.biblioteca.domain.model.Emprestimo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public record EmprestimoAtrasadoResponse(Long id, Long livroId, String livroTitulo, Long leitorId, String leitorNome, LocalDate emprestadoEm, LocalDate previstoPara, long diasAtraso) {
    public static EmprestimoAtrasadoResponse de(Emprestimo domain, LocalDate hoje) {
        long dias = ChronoUnit.DAYS.between(domain.getPrevistoPara(), hoje);
        return new EmprestimoAtrasadoResponse(domain.getId(), domain.getLivro().getId(), domain.getLivro().getTitulo(), domain.getLeitor().getId(), domain.getLeitor().getNome(), domain.getEmprestadoEm(), domain.getPrevistoPara(), Math.max(0, dias));
    }
}