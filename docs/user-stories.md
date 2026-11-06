# Histórias de Usuário e Critérios de Aceite (BDD)

## US01 — Cadastro Seguro de Livros (Prevenção de Duplicidade)
**Como** voluntário da biblioteca comunitária,  
**Quero** cadastrar obras no catálogo com validação de ISBN único,  
**Para que** não ocorra duplicidade de registros físicos no acervo.

### Critério de Aceite CA5 (Gherkin / BDD):
```gherkin
Cenário: Tentativa de cadastrar livro com ISBN duplicado
  Dado que já existe um livro cadastrado com o ISBN "978-85-7522-563-9"
  Quando o voluntário envia uma requisição para cadastrar outro livro com o mesmo ISBN
  Então o sistema deve recusar a operação
  E retornar o status HTTP 409 Conflict com mensagem explicativa no padrão RFC 7807
```

---

## US02 — Consulta Dinâmica de Acervo com Disponibilidade
**Como** voluntário no balcão de atendimento,  
**Quero** consultar livros por termo contido no título ou autor com os exemplares disponíveis atualizados,  
**Para que** eu informe com precisão aos leitores quais títulos podem ser retirados imediatamente.

### Critério de Aceite (Gherkin / BDD):
```gherkin
Cenário: Consulta de disponibilidade calculada em tempo real
  Dado que o livro "O Homem e Seus Símbolos" possui 3 exemplares no total
  E 1 exemplar encontra-se atualmente emprestado com devolução pendente
  Quando o voluntário pesquisa por "Homem"
  Então o resultado deve apresentar o livro com 3 exemplares totais e exatamente 2 exemplares disponíveis
```

---

## US03 — Empréstimo com Reserva e Garantia de Estoque
**Como** voluntário no balcão de atendimento,  
**Quero** emitir um empréstimo assegurando que há cópias físicas livres,  
**Para que** nenhum leitor retire um livro sem estoque real disponível.

### Critério de Aceite CA1 (Gherkin / BDD):
```gherkin
Cenário: Tentativa de emprestar livro com estoque esgotado
  Dado que o livro "Clean Architecture" possui apenas 1 exemplar cadastrado
  E esse único exemplar já se encontra com um empréstimo ativo em aberto
  Quando o voluntário tenta registrar um novo empréstimo deste mesmo livro
  Então o sistema deve recusar o empréstimo
  E retornar o status HTTP 409 Conflict
```

---

## US04 — Limite Máximo de Empréstimos Ativos por Leitor
**Como** gestor da biblioteca comunitária,  
**Quero** limitar em no máximo 2 empréstimos em aberto por leitor,  
**Para que** as obras circulem democraticamente entre toda a comunidade do bairro.

### Critério de Aceite CA2 (Gherkin / BDD):
```gherkin
Cenário: Tentativa de registrar o 3º empréstimo ativo para o mesmo leitor
  Dado que a leitora "Maria Oliveira" já possui 2 empréstimos ativos em aberto
  Quando o voluntário tenta registrar um 3º empréstimo para a leitora "Maria Oliveira"
  Então o sistema deve bloquear o empréstimo
  E retornar o status HTTP 409 Conflict indicando que o limite máximo de 2 livros foi atingido
```

---

## US05 — Prazos de Devolução e Regularização do Acervo
**Como** voluntário e leitor da biblioteca,  
**Quero** que o prazo padrão de devolução seja fixado em 14 dias corridos,  
**Para que** o leitor tenha tempo suficiente de leitura sem prejudicar a rotatividade.

### Critério de Aceite Central (Gherkin / BDD):
```gherkin
Cenário: Cálculo automático da data prevista de devolução
  Dado que um empréstimo foi emitido com sucesso na data de hoje
  Quando a transação é confirmada pelo sistema
  Então a data prevista de devolução gravada deve ser rigorosamente calculada como: Hoje + 14 dias corridos
```

---

## US06 — Devolução de Livro e Restituição Imediata ao Acervo
**Como** voluntário no balcão de atendimento,  
**Quero** dar baixa no empréstimo na devolução física do livro,  
**Para que** o exemplar retorne instantaneamente à disponibilidade do acervo e não permita baixas duplicadas.

### Critério de Aceite CA3 (Gherkin / BDD):
```gherkin
Cenário: Restituição ao estoque e bloqueio de devolução repetida
  Dado um empréstimo ativo de um livro cuja disponibilidade anterior era 0
  Quando o voluntário registra a devolução deste empréstimo
  Então a quantidade de exemplares disponíveis do livro deve aumentar em 1
  E quando o voluntário tentar registrar a devolução novamente para o mesmo ID de empréstimo
  Então o sistema deve recusar com status HTTP 409 Conflict
```

---

## US07 — Monitoramento de Empréstimos em Atraso
**Como** voluntário gestor,  
**Quero** uma listagem filtrada de todos os empréstimos vencidos e não devolvidos,  
**Para que** eu possa entrar em contato com os leitores e solicitar a devolução.

### Critério de Aceite CA4 (Gherkin / BDD):
```gherkin
Cenário: Exibição condicional no relatório de atrasos
  Dado um empréstimo cuja data prevista de devolução é anterior à data de hoje e não possui data de devolução
  Quando o voluntário consulta a rota de atrasados
  Então esse empréstimo deve constar no relatório
  Mas quando a devolução deste empréstimo for confirmada
  Então ele deve deixar de aparecer imediatamente na lista de atrasados
```
