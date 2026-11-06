# Arquitetura, Decisões de Engenharia e Security by Design

## 1. Justificativas Técnicas do Desafio 06

### 1.1. Disponibilidade: Calculada dinamicamente ou mantida por coluna estática?
- **Decisão Adotada:** Cálculo dinâmico em tempo de execução via agregação relacional:
  $$\text{Disponíveis} = \text{Exemplares Totais} - \text{COUNT}(\text{Empréstimos Ativos})$$
  onde empréstimo ativo é aquele com `devolvido_em IS NULL`.
- **Justificativa de Engenharia:**  
  Manter uma coluna física `exemplares_disponiveis` na tabela `livros` geraria o clássico problema de anomalia de atualização concorrente (*race conditions*) e risco crônico de descompasso em caso de falhas parciais de transação ou *rollbacks*.  
  Ao calcular a disponibilidade a partir do estado real da tabela `emprestimos`, eliminamos completamente a redundância e garantimos consistência estrita. Para mitigar o custo computacional, criamos índices compostos em `(livro_id, devolvido_em)`, tornando a agregação $O(\log N)$ com execução instantânea no motor relacional.

### 1.2. Quais regras residem no banco de dados e quais na aplicação?
- **No Banco de Dados (Restrições Estruturais e Integridade Referencial):**
  - Restrição de unicidade em `isbn` (`UNIQUE(isbn)`) para garantia atômica no nível ACID.
  - Restrições de chave estrangeira (`FK_emprestimo_livro`, `FK_emprestimo_leitor`).
  - Restrição de domínio `CHECK (exemplares >= 1)`.
  - Índice em `data_prevista_devolucao` e `devolvido_em` para otimização de consultas temporais.
- **Na Aplicação / Domínio Hexagonal (Regras de Negócio e Políticas Dinâmicas):**
  - Validação do limite máximo de 2 empréstimos simultâneos por leitor.
  - Política de cálculo do prazo padrão de devolução em exatamente 14 dias corridos (`Hoje + 14`).
  - Verificação de estoque disponível antes de autorizar o empréstimo (`HTTP 409 Conflict`).
  - Bloqueio de baixa duplicada em devolução já realizada (`HTTP 409 Conflict`).
  - Bloqueio de renovação para empréstimos que já se encontram vencidos.

---

## 2. Princípios de Arquitetura Hexagonal (Ports & Adapters)

A aplicação foi isolada em círculos concêntricos de dependência:

1. **Domain (Núcleo):** Contém os modelos de domínio puros (`Livro`, `Leitor`, `Emprestimo`, `Isbn`), imunes a anotações JPA ou dependências do framework Spring.
2. **Ports (Interfaces):**
   - **Inbound Ports:** Interfaces que descrevem o que a aplicação faz (ex: `EmprestarLivroUseCase`, `DevolverLivroUseCase`).
   - **Outbound Ports:** Interfaces SPI que definem o que a aplicação precisa do mundo externo (ex: `LivroRepositoryPort`, `ClockPort`).
3. **Adapters (Adaptadores):**
   - **Inbound (Driving):** Controllers REST que recebem requisições HTTP, validam DTOs e acionam os casos de uso.
   - **Outbound (Driven):** Repositórios JPA, entidades de tabela (`LivroJpaEntity`) e mappers que convertem entre o modelo de banco e o modelo de domínio.
