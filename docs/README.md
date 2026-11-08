# Índice de Documentação Técnica — Biblioteca Comunitária

Bem-vindo ao índice central da documentação técnica e de engenharia de software do projeto **Biblioteca Comunitária**.

---

## 1. Mapa de Documentos

| Arquivo | Descrição |
|---|---|
| [`docs/requisitos.md`](requisitos.md) | Catálogo unificado de Requisitos Funcionais (RF01 a RF11), Regras de Negócio e Critérios de Aceite (CA1 a CA5 em BDD). |
| [`docs/rastreabilidade.md`](rastreabilidade.md) | Matriz bidirecional de rastreabilidade mapeando RF ↔ UC ↔ US ↔ Issue ↔ Código ↔ Teste. |
| [`CONTRIBUTING.md`](../CONTRIBUTING.md) | Diretrizes para contribuição, fluxo Git Flow, formato de commits convencionais e comandos de validação local. |
| [`CHANGELOG.md`](../CHANGELOG.md) | Histórico de alterações por versão de lançamento seguindo a convenção *Keep a Changelog*. |
| [`LICENSE`](../LICENSE) | Termos de licenciamento do software sob licença MIT. |
| [`.env.example`](../.env.example) | Modelo descritivo das variáveis de ambiente suportadas pela aplicação. |

---

## 2. Regra de Sincronização Obrigatória (Doc-as-Code)

Nenhum código ou regra de negócio deve ser alterado sem a respectiva atualização documental. Todo Pull Request submetido deve cumprir as seguintes regras:

1. **Alteração em Requisitos ou Comportamentos**:
   - Atualizar a especificação no arquivo `docs/requisitos.md`.
   - Atualizar a matriz bidirecional em `docs/rastreabilidade.md` vinculando a nova Issue e os novos testes implementados.
2. **Nova Funcionalidade ou Correção de Bug**:
   - Atualizar a seção `[Unreleased]` ou a versão alvo no `CHANGELOG.md`.
3. **Novas Variáveis de Configuração**:
   - Atualizar imediatamente o arquivo `.env.example`.

> **Auditoria de CI/Review**: Pull Requests que alterem regras de domínio sem evidência de atualização na matriz de rastreabilidade ou na documentação não serão aprovados para merge na branch `develop`.

---

## 3. Visão Geral da Arquitetura Hexagonal

O projeto adota estritamente a **Arquitetura Hexagonal (Ports & Adapters)**:

```
+------------------------------------------------------------------------------------+
|                                INFRASTRUCTURE ADAPTERS                             |
|                                                                                    |
|  [Inbound / Web Controllers]                 [Outbound / JPA Persistence]          |
|  LivroController                             SpringDataLivroRepository             |
|  EmprestimoController                        SpringDataEmprestimoRepository        |
|  RelatorioController                         SpringDataLeitorRepository            |
|         │                                                 ▲                        |
|         ▼                                                 │                        |
|  +────────────────────────────────────────────────────────┴──────────────────────+ |
|  |                              APPLICATION LAYER                                | |
|  |                                                                               | |
|  |  [Use Cases / Inbound Ports]              [Repository Ports / Outbound Ports] | |
|  |  CadastrarLivroUseCase                    LivroRepositoryPort                 | |
|  |  RealizarEmprestimoUseCase                EmprestimoRepositoryPort            | |
|  |  DevolverLivroUseCase                     LeitorRepositoryPort                | |
|  |  RenovarEmprestimoUseCase                 ClockPort                           | |
|  |                                                                               | |
|  |  +──────────────────────────────────────────────────────────────────────────+ | |
|  |  |                              DOMAIN LAYER                                | | |
|  |  |                                                                          | | |
|  |  |  [Entities & Aggregates]               [Value Objects]                   | | |
|  |  |  Livro                                  Isbn                              | | |
|  |  |  Leitor                                 [Domain Exceptions]               | | |
|  |  |  Emprestimo                             BusinessConflictException (409)   | | |
|  |  |  RankingLivro                           ValidationException (400)         | | |
|  |  +──────────────────────────────────────────────────────────────────────────+ | |
|  +───────────────────────────────────────────────────────────────────────────────+ |
+------------------------------------------------------------------------------------+
```
