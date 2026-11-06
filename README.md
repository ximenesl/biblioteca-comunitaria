# 📚 Biblioteca Comunitária

[![Java Version](https://img.shields.io/badge/Java-21%20LTS-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal%20(Ports%20%26%20Adapters)-blueviolet)](#-arquitetura-do-sistema)
[![Tests](https://img.shields.io/badge/Tests-100%25%20Passing%20(CA1--CA5)-success)](src/test/java/br/com/senac/biblioteca/BibliotecaComunitariaIntegrationTests.java)
[![Security](https://img.shields.io/badge/Security-By%20Design%20(OWASP%20%2B%20RFC%207807)-informational)](#-seguran%C3%A7a-security-by-design)
[![SemVer](https://img.shields.io/badge/SemVer-v1.0.0-orange.svg)](#-versionamento-sem%C3%A2ntico-semver)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Backend corporativo para gestão de acervo físico, empréstimos, devoluções e monitoramento de atrasos em bibliotecas comunitárias de bairro (Desafio 06 — Senac).

---

## 📖 Visão Geral e Stack Tecnológica

A **Biblioteca Comunitária** é uma API REST corporativa desenvolvida para informatizar e profissionalizar o controle operacional de acervo e a circulação de livros em bibliotecas comunitárias mantidas por iniciativas voluntárias. O software resolve problemas críticos de gestão de balcão — como o extravio frequente de obras, a falta de visibilidade em tempo real sobre exemplares físicos disponíveis e a ausência de controle sistemático sobre prazos de devolução — aplicando regras de negócio rigorosas para conferência de estoque, teto máximo de empréstimos simultâneos por leitor e rastreamento imediato de títulos em atraso.

A stack tecnológica foi concebida integralmente sobre o ecossistema moderno **Java 21 LTS** e **Spring Boot 3.4.2**, estruturada segundo as diretrizes da **Arquitetura Hexagonal (Ports and Adapters)**, **Clean Code** e **Domain-Driven Design (DDD)**. O armazenamento e consulta de dados utilizam persistência relacional com **Spring Data JPA** e **Hibernate** sobre banco de dados em memória **H2** (configurado em modo de compatibilidade com PostgreSQL), eliminando a dependência de infraestruturas cloud pagas. A segurança adota a abordagem *Security by Design*, com cabeçalhos HTTP defensivos recomendados pela OWASP, padronização de erros e exceções via **RFC 7807 (ProblemDetail)** e documentação viva interativa com **OpenAPI 3.0 / Swagger UI**.

---

## 🎯 Sumário de Funcionalidades

### Funcionalidades Implementadas (v1.0.0)
- ✅ **[RF01] Cadastro de Livros:** Validação de ISBN canônico único (Value Object), título, autor e quantidade de exemplares ($\ge 1$).
- ✅ **[RF02] Catálogo e Busca Dinâmica:** Pesquisa por título ou autor com cálculo em tempo real de exemplares disponíveis (`total - empréstimos ativos`).
- ✅ **[RF03] Empréstimo com Reserva de Estoque:** Validação de disponibilidade física e prazo padrão de devolução estabelecido em **14 dias corridos**.
- ✅ **[RF04] Limite por Leitor:** Restrição estrita de no máximo 2 empréstimos simultâneos por leitor cadastrado.
- ✅ **[RF05] Devolução e Bloqueio Duplo:** Registro de baixa de empréstimo com restituição imediata ao acervo e bloqueio idempotente contra devoluções repetidas.
- ✅ **[RF06] Relatório de Atrasos:** Listagem filtrada de empréstimos não devolvidos cuja data prevista já expirou em relação à data corrente.
- ✅ **[RF07] Carga Inicial Automatizada:** Povoamento no boot com leitores, catálogo de livros e cenário de empréstimo vencido para validação imediata.
- ✅ **[RF08] Renovação de Empréstimos (Extra):** Prorrogação do prazo em +14 dias, com bloqueio automático caso a obra já esteja com atraso registrado.
- ✅ **[RF09] Histórico e Ranking (Extra):** Extrato individual de empréstimos por leitor e ranking analítico dos livros mais requisitados da biblioteca.

### Funcionalidades Futuras (Planejadas para v1.1.0)
- ⏳ Notificação automatizada de leitores via WhatsApp/E-mail 2 dias antes do vencimento.
- ⏳ Integração com leitor de código de barras físico no balcão.

---

## 🧪 Validação dos Critérios de Aceite (CA1 a CA5)

Todos os critérios de aceitação foram validados com testes de integração automatizados ponta a ponta:

| Critério | Descrição da Regra | Resultado Esperado | Status dos Testes |
|---|---|---|---|
| **CA1** | Tentativa de emprestar livro cujo único exemplar já está emprestado | Retorna `HTTP 409 Conflict` | ✅ Aprovado (`CA1_deveRetornarConflict_quandoNaoHouverExemplaresDisponiveis`) |
| **CA2** | Tentativa de realizar 3º empréstimo ativo para o mesmo leitor | Retorna `HTTP 409 Conflict` | ✅ Aprovado (`CA2_deveRetornarConflict_quandoLeitorAtingirLimiteDoisEmprestimos`) |
| **CA3** | Devolução do livro aumenta disponibilidade; devolução de empréstimo já devolvido | Retorna `HTTP 409 Conflict` | ✅ Aprovado (`CA3_devolucaoRestituiEstoqueEBloqueiaDevolucaoDupla`) |
| **CA4** | Empréstimo com data prevista anterior a hoje aparece em atrasados; após devolução não aparece | Lista atualizada dinamicamente | ✅ Aprovado (`CA4_deveListarEmprestimoAtrasadoApenasEnquantoNaoDevolvido`) |
| **CA5** | Tentativa de cadastrar livro com ISBN já existente na base | Retorna `HTTP 409 Conflict` | ✅ Aprovado (`CA5_deveRetornarConflict_quandoCadastrarIsbnDuplicado`) |
| **Regra Central** | Empréstimo realizado hoje | Data prevista = Hoje + 14 dias | ✅ Aprovado (`deveDefinirPrazoDevolucaoParaQuatorzeDiasCorridos`) |

---

## 🏛️ Arquitetura do Sistema

O projeto adota rigorosamente a **Arquitetura Hexagonal (Ports and Adapters)** para garantir que as regras essenciais do domínio não tenham acoplamento com frameworks ou banco de dados:

```
src/main/java/br/com/senac/biblioteca/
├── domain/                               # NÚCLEO DA APLICAÇÃO (POJOs Puros)
│   ├── model/                            # Livro, Leitor, Emprestimo, Isbn, RankingLivro
│   ├── exception/                        # ValidationException, ResourceNotFoundException, BusinessConflictException
│   └── ports/
│       ├── in/                           # Portas de Entrada (Use Cases)
│       │   ├── CadastrarLivroUseCase.java
│       │   ├── BuscarLivrosUseCase.java
│       │   ├── EmprestarLivroUseCase.java
│       │   ├── DevolverLivroUseCase.java
│       │   └── ConsultarAtrasosUseCase.java
│       └── out/                          # Portas de Saída (SPI / Interfaces de Repositório)
│           ├── LivroRepositoryPort.java
│           ├── LeitorRepositoryPort.java
│           ├── EmprestimoRepositoryPort.java
│           └── ClockPort.java
└── infrastructure/                       # ADAPTADORES EXTERNOS
    ├── adapters/
    │   ├── in/web/                       # REST Controllers, DTOs e RFC 7807 Exception Handler
    │   └── out/persistence/              # Spring Data JPA, H2/PostgreSQL Entities, Repositories e Mappers
    └── configuration/                    # Spring Security, OpenAPI Swagger UI, DataInitializer
```

---

## 🛡️ Segurança (Security by Design)

- **Headers HTTP Defensivos (OWASP):**
  - `Content-Security-Policy: default-src 'self'`
  - `X-Content-Type-Options: nosniff`
  - `X-Frame-Options: DENY`
  - `Strict-Transport-Security: max-age=31536000 ; includeSubDomains`
- **Validação Estrita de Entrada:** `jakarta.validation` em todos os DTOs (`@NotBlank`, `@Positive`, validação de ISBN).
- **Tratamento Padronizado de Erros (RFC 7807):** Respostas no formato `ProblemDetail`, com tipo URI, título e detalhes sem expor stack traces ou dados sensíveis.

---

## 🚀 Como Executar

### Pré-requisitos
- **Java JDK 21+** instalado e configurado no PATH
- **Apache Maven 3.9+** (ou utilizar o wrapper)

### Execução dos Testes Automatizados
```bash
mvn clean test
```

### Execução da Aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### Documentação Interativa (Swagger UI)
Acesse no navegador:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📑 Documentação de Planejamento e Engenharia

Todos os artefatos de engenharia de software produzidos para o projeto estão versionados na pasta [`docs/`](docs/):

- 📋 [**Project Charter (TAP)**](docs/project-charter.md): Justificativa, visão de negócio, objetivos SMART e matriz de escopo.
- 📐 [**Requisitos de Software (RF e RNF)**](docs/requisitos.md): Especificação rastreável de 9 Requisitos Funcionais e 6 Não Funcionais.
- 🎭 [**Casos de Uso (Use Cases)**](docs/casos-de-uso.md): Diagrama conceitual e detalhamento de fluxos principal, alternativo e exceções (UC01 a UC08).
- 👤 [**Histórias de Usuário (User Stories & BDD)**](docs/user-stories.md): Histórias no formato canônico com cenários Gherkin (Dado-Quando-Então).
- 🏛️ [**Decisões Arquiteturais e Justificativas**](docs/arquitetura-decisoes.md): Trade-offs de engenharia, concorrência, consistência e modelo C4.
- 📋 [**Backlog de Issues de Engenharia**](docs/backlog-issues.md): Mapeamento detalhado das 19 issues, labels e critérios de aceite.

---

## 🔀 Fluxo de Branches e Versionamento (Git Flow & SemVer)

O ciclo de vida do repositório seguiu o **Git Flow profissional**, com branches temáticas por issue, múltiplos commits atômicos por branch (Conventional Commits) e integração via Pull Request (`--no-ff`):

| Branch | Propósito | Regra de Merge |
|---|---|---|
| `main` | Produção estável e marcos de release (SemVer) | Somente via PR a partir da `dev` |
| `dev` | Linha principal de desenvolvimento e integração contínua | Recebe PRs das branches de feature e docs |
| `feature/issue-*` | Implementação de funcionalidades do backlog (1 branch por issue) | PR aprovado para `dev` com testes 100% verdes |
| `docs/issue-*` | Artefatos de especificação, planejamento e arquitetura | PR aprovado para `dev` |

### Tags de Versionamento Semântico (SemVer)
- `v0.1.0`: Especificação completa, documentação formal e setup estrutural da aplicação.
- `v0.2.0`: Núcleo de domínio, entidades de negócio e portas hexagonais.
- `v0.5.0`: Adaptadores de persistência JPA, controllers REST e endpoints de catálogo e livro.
- `v0.8.0`: Regras centrais de negócio (CA1, CA2, CA3, CA4, CA5) e devoluções.
- `v1.0.0`: Suíte de testes 100% verde, OpenAPI, segurança defensiva e homologação final.
