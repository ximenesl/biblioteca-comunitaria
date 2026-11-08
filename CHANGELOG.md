# Changelog

Todas as mudanças notáveis deste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-11-08

### Adicionado
- Implementação da Arquitetura Hexagonal (Ports & Adapters) desacoplando núcleo de domínio e infraestrutura.
- Endpoints REST para gestão de acervo (`/api/v1/livros`), empréstimos (`/api/v1/emprestimos`), leitores e relatórios amigáveis (`/api/v1/relatorios`).
- Regra central de negócio: empréstimo com prazo de devolução previsto de 14 dias corridos (`hoje + 14 dias`).
- Critérios de Aceite CA1 a CA5 integralmente implementados:
  - **CA1**: Bloqueio de empréstimo quando estoque do livro estiver esgotado (`409 Conflict`).
  - **CA2**: Limite de no máximo 2 empréstimos ativos simultâneos por leitor (`409 Conflict`).
  - **CA3**: Devolução atômica com recomposição de estoque e bloqueio de devolução duplicada (`409 Conflict`).
  - **CA4**: Identificação de empréstimos em atraso e não devolvidos via query indexada.
  - **CA5**: Validação canônica de ISBN (10 ou 13 dígitos numéricos) e bloqueio de duplicidade (`409 Conflict`).
- Documentação viva interativa OpenAPI 3 / Swagger UI (`/swagger-ui/index.html` e `/v3/api-docs`) com esquemas e exemplos reais de livros (Tolkien, Martin, Evans).
- Matriz de rastreabilidade bidirecional `docs/rastreabilidade.md` mapeando RF ↔ UC ↔ US ↔ Issue ↔ Código ↔ Teste.
- Suite de testes unitários do domínio (`Livro`, `Leitor`, `Emprestimo`, `Isbn`) e casos de uso com Mockito (`RealizarEmprestimoServiceUnitTest`, `DevolucaoERenovacaoServiceUnitTest`).
- Suite de testes de integração (`BibliotecaComunitariaIntegrationTests`, `SecurityHeadersIntegrationTest`).
- Pipeline de integração contínua (CI) multi-estágio (`security`, `qa`, `build`) com Gitleaks, Semgrep e Trivy.
- Licença MIT formalizada em `LICENSE`.

### Modificado
- Padronização e sincronização da numeração canônica de requisitos funcionais (RF01 a RF11) entre `README.md` e `docs/requisitos.md`.
- Alinhamento de compatibilidade de runtime Java 21 LTS e Spring Boot 3.2.8 com Springdoc OpenAPI 2.5.0.

### Segurança
- Aplicação de cabeçalhos de proteção HTTP recomendados pela OWASP (`X-Content-Type-Options: nosniff`, `X-Frame-Options: SAMEORIGIN`, `Referrer-Policy: strict-origin-when-cross-origin`).
- Tratamento semântico padronizado de exceções de negócio e validação segundo RFC 7807 (`ProblemDetail`).
