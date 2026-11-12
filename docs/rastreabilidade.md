# Matriz de Rastreabilidade de Requisitos

Esta matriz estabelece a rastreabilidade bidirecional completa de engenharia de software da **Biblioteca Comunitária**, correlacionando:
**Requisito Funcional (RF)** ↔ **Caso de Uso (UC)** ↔ **User Story (US/BDD)** ↔ **Issue de Engenharia** ↔ **Camada/Código da Solução** ↔ **Método de Teste Automatizado**.

---

## 📊 Matriz Bidirecional de Rastreabilidade

| ID RF | Nome do Requisito Funcional | Caso de Uso (UC) | User Story (US) | Issue(s) | Camada & Componentes de Código | Método de Teste Automatizado |
|:---:|:---|:---:|:---:|:---:|:---|:---|
| **RF01** | Cadastro de Livros com ISBN único | UC01 | US01 (CA5) | #10 | `CadastrarLivroUseCase`<br>`CadastrarLivroService`<br>`LivroController`<br>`LivroRepositoryPort` | `CA5_deveRetornarConflict_quandoCadastrarIsbnDuplicado`<br>`IsbnTest.deveValidarFormatoCanônico` |
| **RF02** | Catálogo e Busca Dinâmica de Disponibilidade | UC02 | US02 | #11 | `BuscarLivrosUseCase`<br>`BuscarLivrosService`<br>`LivroController`<br>`LivroPersistenceAdapter` | `deveBuscarLivrosComDisponibilidadeCalculada`<br>`deveListarLivrosPorTermoBusca` |
| **RF03** | Empréstimo de Livro com Reserva de Estoque | UC03 | US03 (CA1) | #12 | `EmprestarLivroUseCase`<br>`RealizarEmprestimoService`<br>`EmprestimoController`<br>`EmprestimoRepositoryPort` | `CA1_deveRetornarConflict_quandoNaoHouverExemplaresDisponiveis`<br>`RealizarEmprestimoServiceTest.deveBloquearQuandoEstoqueZero` |
| **RF04** | Limite Máximo de 2 Empréstimos por Leitor | UC03 | US04 (CA2) | #13 | `RealizarEmprestimoService`<br>`Leitor`<br>`EmprestimoRepositoryPort` | `CA2_deveRetornarConflict_quandoLeitorAtingirLimiteDoisEmprestimos`<br>`RealizarEmprestimoServiceTest.deveLancarExcecaoQuandoLeitorAtingirLimite` |
| **RF05** | Prazo Padrão de Devolução (14 dias corridos) | UC03 | US05 | #13 | `RealizarEmprestimoService`<br>`ClockPort`<br>`SystemClockAdapter`<br>`Emprestimo` | `deveDefinirPrazoDevolucaoParaQuatorzeDiasCorridos`<br>`RealizarEmprestimoServiceTest.deveCalcularDataPrevistaMaisQuatorzeDias` |
| **RF06** | Devolução de Livro e Bloqueio Duplo | UC04 | US06 (CA3) | #14 | `DevolverLivroUseCase`<br>`DevolverLivroService`<br>`EmprestimoController` | `CA3_devolucaoRestituiEstoqueEBloqueiaDevolucaoDupla`<br>`DevolverLivroServiceTest.deveBloquearDevolucaoDupla` |
| **RF07** | Relatório de Empréstimos Atrasados | UC05 | US07 (CA4) | #15 | `ListarAtrasadosUseCase`<br>`ListarAtrasadosService`<br>`RelatorioController`<br>`EmprestimoController` | `CA4_deveListarEmprestimoAtrasadoApenasEnquantoNaoDevolvido`<br>`ListarAtrasadosServiceTest.deveFiltrarApenasAtrasados` |
| **RF08** | Carga Inicial de Dados Demonstrativa | N/A | N/A | #03, #19 | `DataInitializer`<br>`CommandLineRunner`<br>`SpringDataRepository` | `BibliotecaComunitariaIntegrationTests.contextLoads`<br>`DataInitializerTest.deveCarregarDadosIniciais` |
| **RF09** | Renovação de Prazo com Bloqueio por Atraso | UC06 | US08 | #17 | `RenovarEmprestimoUseCase`<br>`RenovarEmprestimoService`<br>`EmprestimoController` | `RenovarEmprestimoServiceTest.deveProrrogarMaisQuatorzeDias`<br>`RenovarEmprestimoServiceTest.deveBloquearQuandoAtrasado` |
| **RF10** | Histórico Completo de Empréstimos por Leitor | UC07 | US09 | #17 | `ConsultarHistoricoLeitorUseCase`<br>`ConsultarHistoricoLeitorService`<br>`EmprestimoController` | `ConsultarHistoricoLeitorServiceTest.deveRetornarHistoricoCompleto` |
| **RF11** | Ranking de Livros Mais Emprestados | UC08 | US10 | #17 | `ObterRankingLivrosUseCase`<br>`ObterRankingLivrosService`<br>`RelatorioController` | `ObterRankingLivrosServiceTest.deveOrdenarPorTotalEmprestimos` |

---

## 🔒 Rastreabilidade de Requisitos Não Funcionais (RNF)

| ID RNF | Categoria / Descrição | Evidência de Implementação | Verificação / Validação |
|:---:|:---|:---|:---|
| **RNF01** | **Arquitetura Hexagonal (Ports & Adapters)** | Pacotes `domain/model`, `domain/ports/in`, `domain/ports/out`, `infrastructure/adapters/in/web`, `infrastructure/adapters/out/persistence`. | Zero acoplamento de bibliotecas externas no domínio. |
| **RNF02** | **Runtime Java Moderno e Spring Boot** | `pom.xml` com Java 21 LTS e Spring Boot 3.2.8. | Build Maven limpo (`mvn clean test`). |
| **RNF03** | **Security by Design & OWASP Headers** | `SecurityConfig` aplicando CSP, X-Frame-Options (DENY/SAMEORIGIN), X-Content-Type-Options (nosniff) e Referrer-Policy. | `SecurityHeadersIntegrationTest` validando cabeçalhos HTTP. |
| **RNF04** | **Consistência de Estoque Relacional** | Cálculo dinâmico relacional `total - COUNT(*) WHERE devolvido_em IS NULL` com índices em `(livro_id, devolvido_em)`. | Eliminação de race conditions validada nos testes de concorrência e integração. |
| **RNF05** | **Códigos de Status HTTP Semânticos e RFC 7807** | `RestExceptionHandler` tratando `BusinessConflictException` (409), `ValidationException` (400) e `ResourceNotFoundException` (404) via `ProblemDetail`. | `RestExceptionHandlerTest` e asserções nos testes de integração. |
| **RNF06** | **Documentação Viva OpenAPI 3 / Swagger UI** | `OpenApiConfig`, springdoc-openapi e interface interativa `/swagger-ui/index.html`. | Swagger UI carregando com schemas de exemplo e endpoints categorizados. |
| **RNF07** | **Containerização e Qualidade de Imagem Docker** | `Dockerfile` multi-stage (JRE 21 non-root), `docker-compose.yml`, pipeline CI com jobs `docker-sec` (Trivy image scan) e `docker-qa` (healthcheck e smoke test dinâmico). | Varredura de imagem sem vulnerabilidades críticas e contêiner respondendo 200 OK no CI (#56). |
