# Guia de Contribuição — Biblioteca Comunitária

Obrigado pelo interesse em contribuir com o projeto **Biblioteca Comunitária**! Este guia define as diretrizes de engenharia de software, estratégia de ramificação, padrões de commit e processos de qualidade.

---

## 1. Estratégia de Branches (Git Flow)

Adotamos o fluxo de trabalho **Git Flow** estrito:

- `main`: Branch de produção. Contém apenas versões estáveis e aprovadas, identificadas por tags semânticas (ex: `v1.0.0`).
- `develop`: Branch de integração contínua. Todos os Pull Requests de funcionalidades e correções convergem para cá.
- Branches de trabalho por Issue:
  - `feature/issue-<num>-<descricao>`: Novas funcionalidades.
  - `bugfix/issue-<num>-<descricao>` ou `fix/issue-<num>-<descricao>`: Correção de bugs.
  - `docs/issue-<num>-<descricao>`: Documentação e requisitos.
  - `test/issue-<num>-<descricao>`: Novas suites ou refatoração de testes.
  - `sec/issue-<num>-<descricao>`: Segurança e auditoria.
  - `gov/issue-<num>-<descricao>`: Governança de repositório e templates.

> **Regra**: Nunca faça commits diretamente em `main` ou `develop`. Sempre crie uma branch a partir de `develop` e abra um Pull Request.

---

## 2. Convenção de Commits (Conventional Commits)

Todas as mensagens de commit devem seguir a convenção [Conventional Commits](https://www.conventionalcommits.org/):

```
<tipo>(<escopo>): <descrição no imperativo e minúsculo> (#<issue>)
```

### Tipos Permitidos
- `feat`: Nova funcionalidade para o usuário ou domínio.
- `fix`: Correção de bug.
- `docs`: Alterações exclusivamente em documentação.
- `test`: Adição ou correção de testes unitários ou de integração.
- `refactor`: Mudança no código que não corrige bug nem adiciona funcionalidade.
- `sec`: Melhorias de segurança e cabeçalhos OWASP.
- `chore` / `gov`: Tarefas de build, governança, CI ou dependências.

### Exemplos
- `feat(loan): validar limite maximo de 2 emprestimos simultaneos (#18)`
- `fix(ci): reestruturar workflow CI multi-estagio security qa build (#33)`
- `docs(traceability): implementar matriz de rastreabilidade bidirecional (#34)`

---

## 3. Padrão de Pull Requests

1. **Título do PR**: Deve resumir a alteração no padrão Conventional Commits.
2. **Descrição do PR**: Deve conter obrigatoriamente a sintaxe de vinculação para fechamento automático da issue correspondente:
   ```markdown
   Closes #<numero-da-issue>
   ```
3. **Checklist Pré-Merge**:
   - [ ] Branch base correta (`develop`).
   - [ ] Todos os testes locais passando sem falhas (`mvn test`).
   - [ ] Rastreabilidade atualizada em `docs/rastreabilidade.md`.
   - [ ] Pipeline de CI (`maven-ci.yml`) verde (security, qa, build).

---

## 4. Como Executar e Validar Localmente

### Pré-requisitos
- **Java**: 21 LTS (`java -version`)
- **Maven**: 3.9+ (`mvn -version`)

### Comandos Essenciais
```bash
# Compilar código-fonte e validar contratos
mvn compile

# Compilar classes de teste
mvn test-compile

# Executar todos os testes unitários e de integração
mvn test

# Gerar o artefato executável JAR
mvn clean package

# Executar a aplicação localmente
mvn spring-boot:run
```

### Acessar a Documentação Interativa
Com a aplicação em execução:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (`jdbc:h2:mem:bibliotecadb`)

---

## 5. Rastreabilidade de Engenharia

Qualquer alteração em requisitos funcionais ou novos casos de uso exige atualização simultânea em:
1. `docs/requisitos.md`: Detalhamento textual e numeração canônica (RF01 a RF11).
2. `docs/rastreabilidade.md`: Matriz bidirecional RF ↔ UC ↔ US ↔ Issue ↔ Código ↔ Teste.
3. `CHANGELOG.md`: Registro da versão segundo o padrão Keep a Changelog.
