package br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "emprestimos", indexes = {
    @Index(name = "idx_emp_livro_devolvido", columnList = "livro_id, devolvido_em"),
    @Index(name = "idx_emp_leitor_devolvido", columnList = "leitor_id, devolvido_em"),
    @Index(name = "idx_emp_atrasados", columnList = "previsto_para, devolvido_em")
})
public class EmprestimoJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "livro_id", nullable = false)
    private LivroJpaEntity livro;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leitor_id", nullable = false)
    private LeitorJpaEntity leitor;
    @Column(nullable = false) private LocalDate emprestadoEm;
    @Column(nullable = false) private LocalDate previstoPara;
    private LocalDate devolvidoEm;
    @Column(nullable = false) private int quantidadeRenovacoes;

    public EmprestimoJpaEntity() {}
    public EmprestimoJpaEntity(Long id, LivroJpaEntity livro, LeitorJpaEntity leitor, LocalDate emprestadoEm, LocalDate previstoPara, LocalDate devolvidoEm, int quantidadeRenovacoes) {
        this.id = id; this.livro = livro; this.leitor = leitor; this.emprestadoEm = emprestadoEm;
        this.previstoPara = previstoPara; this.devolvidoEm = devolvidoEm; this.quantidadeRenovacoes = quantidadeRenovacoes;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LivroJpaEntity getLivro() { return livro; }
    public void setLivro(LivroJpaEntity livro) { this.livro = livro; }
    public LeitorJpaEntity getLeitor() { return leitor; }
    public void setLeitor(LeitorJpaEntity leitor) { this.leitor = leitor; }
    public LocalDate getEmprestadoEm() { return emprestadoEm; }
    public void setEmprestadoEm(LocalDate emprestadoEm) { this.emprestadoEm = emprestadoEm; }
    public LocalDate getPrevistoPara() { return previstoPara; }
    public void setPrevistoPara(LocalDate previstoPara) { this.previstoPara = previstoPara; }
    public LocalDate getDevolvidoEm() { return devolvidoEm; }
    public void setDevolvidoEm(LocalDate devolvidoEm) { this.devolvidoEm = devolvidoEm; }
    public int getQuantidadeRenovacoes() { return quantidadeRenovacoes; }
    public void setQuantidadeRenovacoes(int quantidadeRenovacoes) { this.quantidadeRenovacoes = quantidadeRenovacoes; }
}