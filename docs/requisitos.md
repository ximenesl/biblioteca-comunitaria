# EspecificaÃ§Ã£o de Requisitos do Sistema

## 1. Requisitos Funcionais (RF)
- **[RF01] Cadastro de Livros:** Cadastro de livro informando tÃ­tulo, autor, ISBN Ãºnico e quantidade de exemplares ($\ge 1$).
- **[RF02] Busca de Livros e Disponibilidade:** Busca por tÃ­tulo ou autor com cÃ¡lculo dinÃ¢mico de exemplares disponÃ­veis (`exemplares - ativos`).
- **[RF03] EmprÃ©stimo de Livro:** EmprÃ©stimo se houver exemplar disponÃ­vel e leitor tiver menos de 2 emprÃ©stimos ativos. Prazo previsto = data + 14 dias.
- **[RF04] DevoluÃ§Ã£o de Livro:** Registro de devoluÃ§Ã£o, restituiÃ§Ã£o ao estoque e bloqueio de devoluÃ§Ã£o redundante.
- **[RF05] Listagem de EmprÃ©stimos Atrasados:** EmprÃ©stimos ativos com `previsto_para < hoje` e `devolvido_em IS NULL`.
- **[RF06] Carga Inicial de Dados:** Leitores fictÃ­cios e acervo demonstrativo carregados no boot.
- **[RF07] RenovaÃ§Ã£o de EmprÃ©stimo (Extra):** ProrrogaÃ§Ã£o de +14 dias se o emprÃ©stimo nÃ£o estiver em atraso.
- **[RF08] HistÃ³rico por Leitor (Extra):** Consulta de todos os emprÃ©stimos realizados por um leitor.
- **[RF09] Ranking de Livros (Extra):** Obras ordenadas por quantidade total de emprÃ©stimos histÃ³ricos.

## 2. Requisitos NÃ£o Funcionais (RNF)
- **[RNF01] Arquitetura Hexagonal:** DomÃ­nio desacoplado de frameworks e banco de dados.
- **[RNF02] Runtime Java Moderno:** ConstruÃ­do sobre Java LTS e Spring Boot 3.4.2.
- **[RNF03] Security by Design:** ValidaÃ§Ã£o declarativa, OWASP headers e ProblemDetail (RFC 7807).
- **[RNF04] ConsistÃªncia de Estoque:** CÃ¡lculo relacional dinÃ¢mico de disponÃ­veis (`COUNT(*) WHERE devolvido_em IS NULL`).
- **[RNF05] CÃ³digos HTTP REST:** Conflitos de negÃ³cio retornam HTTP 409 Conflict.
- **[RNF06] DocumentaÃ§Ã£o OpenAPI 3:** Swagger UI acessÃ­vel em `/swagger-ui/index.html`.