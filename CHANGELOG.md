# Changelog

Todas as mudanças notáveis deste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.2.0] - 2026-11-11

### Adicionado
- `Dockerfile` multi-stage (builder: `maven:3.9.9-eclipse-temurin-21-alpine`, runner: `eclipse-temurin:21-jre-alpine`) com usuário non-root `spring:spring`, `HEALTHCHECK` e `JAVA_OPTS` para containers.
- `.dockerignore` excluindo artefatos desnecessários da imagem.
- `docker-compose.yml` para execução local completa sem configuração adicional.
- Pipeline CI estendida com 5 estágios: `security → qa → docker-sec → docker-qa → build`.
  - `docker-sec`: build da imagem, Trivy image scan e auditoria de usuário non-root.
  - `docker-qa`: smoke test dinâmico do container (HTTP 200, `/v3/api-docs`, seed data).
- `docs/rastreabilidade.md`: RNF07 adicionado para rastreabilidade de Docker.
- `README.md`: seção "Execução via Docker ou Docker Compose" com comandos prontos.

## [2.1.0] - 2026-11-11

### Corrigido
- **Swagger 500**: corrige `NullPointerException` no Springdoc ao gerar `/v3/api-docs` causado por `@RestControllerAdvice(basePackages = ...)` combinado com handlers de `ProblemDetail`.
- Atualização do `springdoc-openapi-starter-webmvc-ui` 2.5.0 → 2.6.0 (fix oficial para NPE com `NoResourceFoundException`).
- Propriedades `springdoc.override-with-generic-response=false` e `springdoc.default-produces-media-type` adicionadas para estabilidade do gerador de spec.

### Melhorado
- `OpenApiConfig` reescrito com `Info` completa, `Contact`, `License` e `Server` declarativo.
- Todos os controllers anotados com `@Tag`, `@Operation`, `@ApiResponse` e `@ExampleObject`:
  - **Livros**: 3 exemplos reais de cadastro (Tolkien, Clean Code, Dom Casmurro).
  - **Emprestimos**: exemplos de empréstimo, devolução, renovação e histórico por leitor.
  - **Relatorios**: descrições dos endpoints de ranking e atrasados.

## [2.0.0] - 2026-11-08

### Adicionado
- Atendimento a 100% dos apontamentos da Avaliação Técnica Semanal (Issues #18 a #32).
- Containerização oficial com `Dockerfile` multi-stage (JRE 21 Alpine, usuário non-root `spring:spring`, healthcheck) e `docker-compose.yml` (#56).
- Pipeline CI estendido com validação de segurança e qualidade da imagem Docker: `security` (repo) ➔ `qa` (testes) ➔ `docker-sec` (Trivy image scan) ➔ `docker-qa` (smoke test dinâmico e validação de healthcheck do contêiner) ➔ `build` (#56).
- Matriz de rastreabilidade bidirecional `docs/rastreabilidade.md` cobrindo RF ↔ UC ↔ US ↔ Issue ↔ Código ↔ Teste.
- Suíte completa de testes unitários isolados com Mockito para domínio (`Livro`, `Leitor`, `Emprestimo`, `Isbn`) e casos de uso (`RealizarEmprestimoServiceUnitTest`, `DevolucaoERenovacaoServiceUnitTest`).
- Suíte de testes de integração para validação de cabeçalhos OWASP (`SecurityHeadersIntegrationTest`).
- Configuração de governança com `.github/dependabot.yml`, templates de issue e PR, `.env.example`, `CONTRIBUTING.md` e `LICENSE` MIT.
- Índice geral e regras estritas de sincronização doc-as-code em `docs/README.md`.
- Exemplos realistas e schemas completos no Swagger UI / OpenAPI 3 com Tolkien, Evans e Martin.

### Modificado
- Unificação e sincronização canônica de RF01 a RF11 entre `README.md` e `docs/requisitos.md`.
- Estabilidade garantida do endpoint `/v3/api-docs` (200 OK) no ecossistema Spring Boot com Java 21 LTS.

## [1.0.0] - 2026-11-05
### Adicionado
- Implementação inicial da Arquitetura Hexagonal, CRUD de livros, empréstimos e regras centrais de negócio (CA1 a CA5).

### Segurança
- Aplicação de cabeçalhos de proteção HTTP recomendados pela OWASP (`X-Content-Type-Options: nosniff`, `X-Frame-Options: SAMEORIGIN`, `Referrer-Policy: strict-origin-when-cross-origin`).
- Tratamento semântico padronizado de exceções de negócio e validação segundo RFC 7807 (`ProblemDetail`).
