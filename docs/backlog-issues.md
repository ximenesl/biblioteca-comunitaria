# Backlog de Engenharia do Repositório

## 📋 Issues de Documentação e Planejamento (2 Issues)
- **[Issue #01] docs(plan): Planejamento e Especificação Formal do Software**
  - Labels: `documentation`, `specification`
  - Escopo: Project Charter, Requisitos RF/RNF, Casos de Uso detalhados e User Stories com critérios BDD.
- **[Issue #02] docs(arch): Decisões Arquiteturais Hexagonais, C4 Model e Justificativas de Engenharia**
  - Labels: `documentation`, `architecture`
  - Escopo: Trade-off de cálculo de disponibilidade em tempo real vs coluna estática, distribuição de regras entre banco e aplicação.

---

## ⚙️ Issues de Desenvolvimento de Software (15 Issues)
- **[Issue #03] chore(setup): Setup do Projeto Spring Boot com Java, Maven e Workflow CI**
  - Labels: `chore`, `setup`
- **[Issue #04] feat(domain): Modelagem das Entidades Puras de Domínio (Livro, Leitor, Emprestimo)**
  - Labels: `feature`, `domain`
- **[Issue #05] feat(domain): Value Object Isbn com Sanitização e Validação Defensiva**
  - Labels: `feature`, `domain`
- **[Issue #06] feat(ports): Definição das Portas Hexagonais de Entrada (Use Cases) e Saída (Repositories)**
  - Labels: `feature`, `architecture`
- **[Issue #07] feat(persistence): Entidades Relacionais JPA e Constraints de Integridade (CHECK, UNIQUE, FK)**
  - Labels: `feature`, `database`
- **[Issue #08] feat(persistence): Mapeamento Bidirecional de Persistência (Mappers Domínio <-> JPA)**
  - Labels: `feature`, `persistence`
- **[Issue #09] feat(persistence): Adaptadores de Saída de Persistência e Clock do Sistema**
  - Labels: `feature`, `persistence`
- **[Issue #10] feat(livro): Caso de Uso e Endpoint REST de Cadastro de Livros com ISBN Único (RF1, CA5)**
  - Labels: `feature`, `web`
- **[Issue #11] feat(busca): Busca de Acervo com Cálculo Dinâmico de Exemplares Disponíveis (RF2)**
  - Labels: `feature`, `web`
- **[Issue #12] feat(emprestimo): Empréstimo com Validação de Exemplar Disponível (RF3, CA1)**
  - Labels: `feature`, `business-rule`
- **[Issue #13] feat(emprestimo): Limite de 2 Empréstimos por Leitor e Prazo Padrão de 14 Dias (RF3, CA2)**
  - Labels: `feature`, `business-rule`
- **[Issue #14] feat(devolucao): Devolução de Exemplar e Bloqueio de Devolução Dupla (RF4, CA3)**
  - Labels: `feature`, `business-rule`
- **[Issue #15] feat(atrasos): Relatório de Empréstimos Atrasados com Auditoria Temporal (RF5, CA4)**
  - Labels: `feature`, `web`
- **[Issue #16] feat(security): Security by Design, Headers OWASP e Tratamento Centralizado RFC 7807**
  - Labels: `feature`, `security`
- **[Issue #17] feat(extras): Funcionalidades Extras: Renovação, Histórico por Leitor e Ranking**
  - Labels: `feature`, `enhancement`
- **[Issue #18] test(e2e): Bateria Completa de Testes Automatizados para Evidências CA1 a CA5**
  - Labels: `test`, `qa`
- **[Issue #19] feat(infra): Carga Inicial de Dados (DataInitializer), OpenAPI / Swagger UI e README Exemplar**
  - Labels: `documentation`, `infrastructure`
