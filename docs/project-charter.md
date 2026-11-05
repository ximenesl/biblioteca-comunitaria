# Project Charter â€” Termo de Abertura do Projeto (TAP)

## 1. IdentificaÃ§Ã£o do Projeto
- **Nome do Projeto:** Biblioteca ComunitÃ¡ria (Sistema de GestÃ£o de Acervo e EmprÃ©stimos de Bairro)
- **Desafio:** Desafio 06 â€” Cloud Challenge / Senac 2026.2
- **Linguagem & Plataforma:** Java 21 LTS, Spring Boot 3.4.2
- **PadrÃ£o Arquitetural:** Arquitetura Hexagonal (Ports and Adapters), Clean Code, Domain-Driven Design, Security by Design
- **PersistÃªncia:** Relacional (H2 / PostgreSQL Compatible Mode com JPA/Hibernate)

---

## 2. Justificativa e VisÃ£o Geral do Produto
Em bibliotecas comunitÃ¡rias de bairro mantidas por voluntÃ¡rios, o controle de acervo e emprÃ©stimo frequentemente ocorre de forma manual em cadernos. Isso gera falta de visibilidade do estoque real, inseguranÃ§a de posse e ausÃªncia de relatÃ³rios de atraso.
O sistema **Biblioteca ComunitÃ¡ria** Ã© uma API REST corporativa desenvolvida para ser operada pelo voluntÃ¡rio no balcÃ£o da biblioteca, automatizando e auditando as operaÃ§Ãµes de busca, cadastro, emprÃ©stimo, devoluÃ§Ã£o e acompanhamento de atrasos com tolerÃ¢ncia zero a inconsistÃªncias.

---

## 3. Objetivos SMART

| Meta | CritÃ©rio SMART | DescriÃ§Ã£o |
|---|---|---|
| **S** (EspecÃ­fico) | Automatizar controle de exemplares e devoluÃ§Ãµes | Registrar emprÃ©stimos e devoluÃ§Ãµes por exemplar Ãºnico com prazo padrÃ£o de 14 dias corridos. |
| **M** (MensurÃ¡vel) | 100% dos critÃ©rios de aceitaÃ§Ã£o validados por testes | Cobrir 100% dos fluxos de negÃ³cio crÃ­ticos (CA1 a CA5) com testes automatizados. |
| **A** (AtingÃ­vel) | Escopo enxuto em Arquitetura Hexagonal | Implementar a soluÃ§Ã£o utilizando Java e Spring Boot sem acoplamento a nuvem. |
| **R** (Relevante) | ResoluÃ§Ã£o de inconsistÃªncias de concorrÃªncia | Eliminar conflitos de estoque calculando a disponibilidade em tempo real (`exemplares - emprÃ©stimos ativos`). |
| **T** (Temporal) | Entrega em ciclo Ã¡gil com SDLC | ExecuÃ§Ã£o via Git Flow, com backlog de engenharia rastreÃ¡vel e commits convencionais. |

---

## 4. Escopo do Projeto

### 4.1. In-Scope
- **GestÃ£o de Livros:** Cadastro com tÃ­tulo, autor, ISBN Ãºnico e exemplares ($\ge 1$).
- **Busca de Acervo:** Filtro por tÃ­tulo ou autor, com cÃ¡lculo dinÃ¢mico de disponÃ­veis.
- **EmprÃ©stimo:** ValidaÃ§Ã£o de exemplar disponÃ­vel, teto de 2 emprÃ©stimos simultÃ¢neos por leitor e devoluÃ§Ã£o prevista para 14 dias.
- **DevoluÃ§Ã£o:** Registro da devoluÃ§Ã£o e bloqueio de devoluÃ§Ã£o duplicada (HTTP 409).
- **RelatÃ³rio de Atrasos:** Listagem de emprÃ©stimos com prazo expirado e nÃ£o devolvidos.
- **Extras:** RenovaÃ§Ã£o de prazo, histÃ³rico do leitor e ranking de obras mais emprestadas.
- **SeguranÃ§a & Boas PrÃ¡ticas:** Zero trust na validaÃ§Ã£o, RFC 7807 (ProblemDetail) e testes de integraÃ§Ã£o automatizados.

### 4.2. Out-of-Scope
- ServiÃ§os externos de computaÃ§Ã£o em nuvem proprietÃ¡rios.
- CobranÃ§a de multas em dinheiro ou gateways bancÃ¡rios.