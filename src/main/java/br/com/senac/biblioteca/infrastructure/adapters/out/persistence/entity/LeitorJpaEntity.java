package br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "leitores")
public class LeitorJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150) private String nome;
    public LeitorJpaEntity() {}
    public LeitorJpaEntity(Long id, String nome) { this.id = id; this.nome = nome; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}