# HistÃ³rias de UsuÃ¡rio e CritÃ©rios BDD (CA1 a CA5)

## US01 â€” Cadastro Seguro de Livro
Como voluntÃ¡rio, quero cadastrar livros com ISBN Ãºnico para catalogar o acervo.
- **CA5 (BDD):** Dado que o ISBN "978-8575225639" jÃ¡ existe, quando tento cadastrÃ¡-lo novamente, entÃ£o o sistema responde com HTTP 409 Conflict.

## US02 â€” Busca de Livros com Disponibilidade
Como voluntÃ¡rio, quero buscar livros por tÃ­tulo ou autor vendo disponÃ­veis em tempo real.

## US03 â€” EmprÃ©stimo com Controle de Limite e Estoque
Como voluntÃ¡rio, quero registrar emprÃ©stimos com prazo de 14 dias, garantindo limite e estoque.
- **CA1 (BDD):** Dado um livro com 1 exemplar jÃ¡ emprestado, quando tento realizar novo emprÃ©stimo, entÃ£o o sistema retorna HTTP 409 Conflict.
- **CA2 (BDD):** Dado um leitor com 2 emprÃ©stimos ativos, quando solicita o 3Âº emprÃ©stimo, entÃ£o o sistema retorna HTTP 409 Conflict.

## US04 â€” DevoluÃ§Ã£o e RestituiÃ§Ã£o de Estoque
Como voluntÃ¡rio, quero registrar devoluÃ§Ãµes fÃ­sicas liberando o exemplar.
- **CA3 (BDD):** Dado um emprÃ©stimo devolvido, a disponibilidade aumenta em 1. Se tento devolver o mesmo emprÃ©stimo novamente, entÃ£o o sistema retorna HTTP 409 Conflict.

## US05 â€” IdentificaÃ§Ã£o de EmprÃ©stimos Atrasados
Como gestor, quero listar emprÃ©stimos vencidos para cobrar devoluÃ§Ãµes.
- **CA4 (BDD):** EmprÃ©stimo previsto para ontem e nÃ£o devolvido consta na lista de atrasados; se jÃ¡ devolvido, nÃ£o consta.