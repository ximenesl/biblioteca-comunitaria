# Especificação de Requisitos do Sistema

## 1. Requisitos Funcionais (RF)

| ID | Nome do Requisito | Descrição Detalhada |
|:---:|:---|:---|
| **[RF01]** | **Cadastro de Livros com ISBN Único** | O sistema deve permitir o cadastro de novas obras informando título, autor, ISBN único e quantidade de cópias físicas ($\ge 1$). |
| **[RF02]** | **Busca de Livros e Catálogo Dinâmico** | O sistema deve permitir a busca por título ou autor com cálculo dinâmico e em tempo real dos exemplares disponíveis (`total - empréstimos ativos`). |
| **[RF03]** | **Empréstimo com Validação de Estoque** | O sistema deve permitir o empréstimo de um livro somente se houver exemplar disponível no acervo físico, bloqueando com `HTTP 409 Conflict` caso o estoque esteja zerado (CA1). |
| **[RF04]** | **Limite de Empréstimos por Leitor** | O sistema deve restringir a concessão de empréstimos garantindo que cada leitor possua no máximo 2 empréstimos simultaneamente ativos, bloqueando o 3º com `HTTP 409 Conflict` (CA2). |
| **[RF05]** | **Prazo Legal de Devolução (14 dias)** | O sistema deve calcular automaticamente a data prevista de devolução fixada em exatamente 14 dias corridos a partir da data de concessão do empréstimo. |
| **[RF06]** | **Devolução de Exemplar e Bloqueio Duplo** | O sistema deve registrar a devolução física da obra, restituindo imediatamente a disponibilidade ao acervo e bloqueando qualquer tentativa de devolução redundante com `HTTP 409 Conflict` (CA3). |
| **[RF07]** | **Relatório de Empréstimos Atrasados** | O sistema deve listar todos os empréstimos cuja data prevista de devolução seja estritamente anterior à data corrente e cuja devolução ainda não tenha ocorrido (CA4). |
| **[RF08]** | **Carga Inicial Automatizada de Dados** | O sistema deve inicializar em ambiente de desenvolvimento com acervo demonstrativo, leitores cadastrados e cenário pré-configurado de atraso para testes imediatos. |
| **[RF09]** | **Renovação de Prazo com Bloqueio de Atraso** | O sistema deve permitir a prorrogação de prazo por mais 14 dias corridos, bloqueando com `HTTP 409 Conflict` caso a obra já se encontre com atraso registrado. |
| **[RF10]** | **Histórico de Empréstimos por Leitor** | O sistema deve fornecer extrato completo de todos os empréstimos (ativos e concluídos) vinculados a um determinado leitor. |
| **[RF11]** | **Ranking Analítico de Obras Populares** | O sistema deve gerar ranking das obras mais requisitadas na biblioteca comunitária ordenadas pela quantidade histórica de empréstimos. |

---

## 2. Requisitos Não Funcionais (RNF)

| ID | Nome do Requisito Não Funcional | Descrição Técnica |
|:---:|:---|:---|
| **[RNF01]** | **Arquitetura Hexagonal (Ports & Adapters)** | Núcleo de domínio estritamente isolado de frameworks web e mecanismos de persistência relacional. |
| **[RNF02]** | **Runtime Java Moderno & Spring Boot** | Construído sobre Java 21 LTS e Spring Boot 3.2.8. |
| **[RNF03]** | **Security by Design & OWASP Headers** | Validação declarativa rigorosa (`jakarta.validation`), cabeçalhos de segurança (CSP, HSTS, X-Frame-Options) e tratamento padronizado via RFC 7807 (`ProblemDetail`). |
| **[RNF04]** | **Consistência Relacional de Estoque** | Eliminação de colunas estáticas de disponibilidade; cálculo em tempo de execução via agregação relacional com índices em `(livro_id, devolvido_em)`. |
| **[RNF05]** | **Códigos Semânticos HTTP REST** | Retorno estrito de `HTTP 201 Created`, `HTTP 200 OK`, `HTTP 400 Bad Request`, `HTTP 404 Not Found` e `HTTP 409 Conflict` para violações de negócio. |
| **[RNF06]** | **Documentação Viva OpenAPI 3** | Swagger UI interativo disponível em `/swagger-ui/index.html` com schemas e exemplos realistas. |

---

## 🔗 Matriz de Rastreabilidade

Consulte o documento completo e detalhado da matriz em: [docs/rastreabilidade.md](rastreabilidade.md).
