# Especificação de Requisitos do Sistema

## 1. Requisitos Funcionais (RF)
- **[RF01] Cadastro de Livros:** Cadastro de livro informando título, autor, ISBN único e quantidade de exemplares ($\ge 1$).
- **[RF02] Busca de Livros e Disponibilidade:** Busca por título ou autor com cálculo dinâmico de exemplares disponíveis (`exemplares - ativos`).
- **[RF03] Empréstimo de Livro:** Empréstimo se houver exemplar disponível e leitor tiver menos de 2 empréstimos ativos. Prazo previsto = data + 14 dias corridos.
- **[RF04] Devolução de Livro:** Registro de devolução, restituição imediata ao acervo e bloqueio de devolução redundante/duplicada.
- **[RF05] Listagem de Empréstimos Atrasados:** Empréstimos ativos com `previsto_para < hoje` e `devolvido_em IS NULL`.
- **[RF06] Carga Inicial de Dados:** Leitores fictícios e acervo demonstrativo carregados no boot da aplicação.
- **[RF07] Renovação de Empréstimo (Extra):** Prorrogação de +14 dias se o empréstimo não estiver em atraso.
- **[RF08] Histórico por Leitor (Extra):** Consulta de todos os empréstimos realizados por um determinado leitor.
- **[RF09] Ranking de Livros (Extra):** Obras ordenadas por quantidade total de empréstimos históricos.

## 2. Requisitos Não Funcionais (RNF)
- **[RNF01] Arquitetura Hexagonal:** Domínio desacoplado de frameworks e banco de dados via Ports and Adapters.
- **[RNF02] Runtime Java Moderno:** Construído sobre Java 21 LTS e Spring Boot 3.4.2.
- **[RNF03] Security by Design:** Validação declarativa, OWASP headers e ProblemDetail (RFC 7807).
- **[RNF04] Consistência de Estoque:** Cálculo relacional dinâmico de disponíveis (`COUNT(*) WHERE devolvido_em IS NULL`).
- **[RNF05] Códigos HTTP REST:** Conflitos de negócio retornam HTTP 409 Conflict; validações inválidas retornam HTTP 400 Bad Request; entidades inexistentes retornam HTTP 404 Not Found.
- **[RNF06] Documentação OpenAPI 3:** Swagger UI acessível e interativo em `/swagger-ui/index.html`.
