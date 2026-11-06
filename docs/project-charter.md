# Project Charter — Termo de Abertura do Projeto (TAP)

## 1. Identificação do Projeto
- **Nome do Projeto:** Biblioteca Comunitária (Sistema de Gestão de Acervo e Empréstimos de Bairro)
- **Desafio:** Desafio 06 — Cloud Challenge / Senac 2026.2
- **Linguagem & Plataforma:** Java 21 LTS, Spring Boot 3.2.8
- **Padrão Arquitetural:** Arquitetura Hexagonal (Ports and Adapters), Clean Code, Domain-Driven Design, Security by Design
- **Persistência:** Relacional (H2 / PostgreSQL Compatible Mode com JPA/Hibernate)

---

## 2. Justificativa e Visão Geral do Produto
Em bibliotecas comunitárias de bairro mantidas por voluntários, o controle de acervo e empréstimo frequentemente ocorre de forma manual em cadernos. Isso gera falta de visibilidade do estoque real, extravio de livros e ausência de relatórios de atraso.
O sistema **Biblioteca Comunitária** é uma API REST corporativa desenvolvida para ser operada pelo voluntário no balcão da biblioteca, automatizando e auditando as operações de busca, cadastro, empréstimo, devolução e acompanhamento de atrasos com tolerância zero a inconsistências.

---

## 3. Objetivos SMART

| Meta | Critério SMART | Descrição |
|---|---|---|
| **S** (Específico) | Automatizar controle de exemplares e devoluções | Registrar empréstimos e devoluções por exemplar único com prazo padrão de 14 dias corridos. |
| **M** (Mensurável) | 100% dos critérios de aceitação validados por testes | Cobrir 100% dos fluxos de negócio críticos (CA1 a CA5) com testes automatizados. |
| **A** (Atingível) | Escopo enxuto em Arquitetura Hexagonal | Implementar a solução utilizando Java e Spring Boot sem acoplamento a serviços pagos de nuvem. |
| **R** (Relevante) | Resolução de inconsistências de concorrência | Eliminar conflitos de estoque calculando a disponibilidade em tempo real (`exemplares - empréstimos ativos`). |
| **T** (Temporal) | Entrega em ciclo ágil com SDLC | Execução via Git Flow, com backlog de engenharia rastreável e commits convencionais. |

---

## 4. Escopo do Projeto

### 4.1. In-Scope
- **Gestão de Livros:** Cadastro com título, autor, ISBN único e exemplares ($\ge 1$).
- **Busca de Acervo:** Filtro por título ou autor, com cálculo dinâmico de disponíveis.
- **Empréstimo:** Validação de exemplar disponível, teto de 2 empréstimos simultâneos por leitor e devolução prevista para 14 dias.
- **Devolução:** Registro da devolução e bloqueio de devolução duplicada (HTTP 409).
- **Relatório de Atrasos:** Listagem de empréstimos com prazo expirado e não devolvidos.
- **Extras:** Renovação de prazo, histórico do leitor e ranking de obras mais emprestadas.
- **Segurança & Boas Práticas:** Zero trust na validação, RFC 7807 (ProblemDetail) e testes de integração automatizados.

### 4.2. Out-of-Scope
- Serviços externos de computação em nuvem proprietários.
- Cobrança de multas em dinheiro ou gateways bancários.
- Autenticação federada complexa OAuth2/SAML.
