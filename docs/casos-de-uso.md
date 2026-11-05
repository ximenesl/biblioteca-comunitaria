# EspecificaÃ§Ã£o de Casos de Uso (Use Cases)

- **UC01: Cadastrar Livro** â€” VoluntÃ¡rio cadastra nova obra. ExceÃ§Ãµes: Dados invÃ¡lidos (400) ou ISBN duplicado (409 - CA5).
- **UC02: Buscar Livros** â€” VoluntÃ¡rio consulta obras e visualiza estoque total e disponÃ­vel em tempo real.
- **UC03: Realizar EmprÃ©stimo** â€” Vincula livro e leitor, data prevista = +14 dias. ExceÃ§Ãµes: Livro sem exemplar (409 - CA1) ou leitor com 2 ativos (409 - CA2).
- **UC04: Registrar DevoluÃ§Ã£o** â€” Marca data de devoluÃ§Ã£o e aumenta disponÃ­veis em 1. ExceÃ§Ã£o: EmprÃ©stimo jÃ¡ devolvido (409 - CA3).
- **UC05: Listar EmprÃ©stimos Atrasados** â€” Lista emprÃ©stimos vencidos e nÃ£o devolvidos (CA4).
- **UC06: Renovar EmprÃ©stimo** â€” Prorroga prazo se nÃ£o estiver em atraso. ExceÃ§Ã£o: Vencido (409).
- **UC07: HistÃ³rico por Leitor** â€” Rastreia emprÃ©stimos do leitor.
- **UC08: Ranking de Livros** â€” Obras mais requisitadas na biblioteca.