# Especificação de Casos de Uso (Use Cases)

## Visão Geral dos Atores
- **Voluntário / Bibliotecário:** Operador responsável pelo atendimento no balcão, registro de acervo, empréstimos, devoluções e verificação de atrasos.
- **Sistema Automatizado:** Mecanismo responsável pelo cálculo em tempo real de disponibilidade, conferência de prazos e bloqueio contra operações inconsistentes.

---

## Casos de Uso Detalhados

### UC01: Cadastrar Livro
- **Ator:** Voluntário
- **Fluxo Principal:** O operador informa título, autor, ISBN (10 ou 13 dígitos) e quantidade de exemplares ($\ge 1$). O sistema persiste a obra e confirma o cadastro.
- **Exceção 1A:** Dados obrigatórios em branco ou inválidos $\to$ Retorna `HTTP 400 Bad Request`.
- **Exceção 1B (CA5):** ISBN já cadastrado no acervo $\to$ Retorna `HTTP 409 Conflict`.

### UC02: Buscar Livros no Acervo
- **Ator:** Voluntário
- **Fluxo Principal:** O operador pesquisa por termo contido no título ou autor. O sistema retorna a lista de obras correspondentes com o cálculo dinâmico dos exemplares totais e disponíveis em tempo real.

### UC03: Realizar Empréstimo de Livro
- **Ator:** Voluntário
- **Fluxo Principal:** O operador seleciona o livro e o leitor. O sistema valida se há exemplares disponíveis e se o leitor possui menos de 2 empréstimos em aberto. A data prevista de devolução é calculada para **14 dias corridos** a partir da data atual.
- **Exceção 3A (CA1):** Quantidade disponível igual a zero $\to$ Retorna `HTTP 409 Conflict`.
- **Exceção 3B (CA2):** Leitor já possui 2 empréstimos ativos $\to$ Retorna `HTTP 409 Conflict`.

### UC04: Registrar Devolução de Livro
- **Ator:** Voluntário
- **Fluxo Principal:** O operador informa o identificador do empréstimo. O sistema grava o timestamp de devolução e o exemplar retorna imediatamente à disponibilidade do acervo.
- **Exceção 4A (CA3):** Empréstimo já se encontra devolvido $\to$ Retorna `HTTP 409 Conflict`.

### UC05: Listar Empréstimos em Atraso (CA4)
- **Ator:** Voluntário
- **Fluxo Principal:** O sistema lista todos os empréstimos ativos cuja data prevista de devolução é estritamente anterior à data corrente e cuja data de devolução seja nula.

### UC06: Renovar Empréstimo
- **Ator:** Voluntário
- **Fluxo Principal:** O operador solicita prorrogação de prazo de um empréstimo em aberto. O sistema estende a data prevista em mais 14 dias.
- **Exceção 6A:** Empréstimo com data prevista já expirada $\to$ Retorna `HTTP 409 Conflict` (bloqueio de renovação para obras em atraso).

### UC07: Consultar Histórico do Leitor
- **Ator:** Voluntário
- **Fluxo Principal:** O sistema retorna a relação completa de todos os empréstimos (ativos e finalizados) vinculados ao leitor especificado.

### UC08: Visualizar Ranking de Livros Mais Emprestados
- **Ator:** Voluntário
- **Fluxo Principal:** O sistema agrega o histórico de empréstimos por obra e exibe o ranking em ordem decrescente de popularidade.
