# Arquitetura, DecisÃµes de Engenharia e Security by Design

## 1. Justificativas das Perguntas do Desafio 06

### DisponÃ­veis = exemplares âˆ’ emprÃ©stimos ativos (calculado) ou mantida por coluna?
**DecisÃ£o:** CÃ¡lculo dinÃ¢mico em tempo de execuÃ§Ã£o via agregaÃ§Ã£o relacional (`exemplares - COUNT(*) WHERE livro_id = :id AND devolvido_em IS NULL`).
**Justificativa:** Elimina condiÃ§Ãµes de corrida (race conditions) e corrupÃ§Ã£o de estado decorrente de falhas parciais. Com Ã­ndices em `(livro_id, devolvido_em)`, a consulta tem custo O(log N).

### Quais regras ficam no banco e quais na aplicaÃ§Ã£o?
- **No Banco:** Integridade referencial (FK), unicidade (`UNIQUE(isbn)`), restriÃ§Ã£o de domÃ­nio (`CHECK (exemplares >= 1)`).
- **Na AplicaÃ§Ã£o:** Regras de negÃ³cio dinÃ¢micas (limite de 2 emprÃ©stimos ativos, devoluÃ§Ã£o prevista em +14 dias, bloqueio de devoluÃ§Ã£o dupla e renovaÃ§Ã£o).

## 2. Arquitetura Hexagonal (Ports and Adapters)
- `domain/model`: Entidades e Value Objects puros.
- `domain/ports/in`: Interfaces de Casos de Uso.
- `domain/ports/out`: Interfaces de RepositÃ³rios e ServiÃ§os de Infraestrutura.
- `application/usecase`: OrquestraÃ§Ã£o de negÃ³cio.
- `infrastructure/adapters/in/web`: REST Controllers e DTOs.
- `infrastructure/adapters/out/persistence`: Adaptadores JPA e Mappers.