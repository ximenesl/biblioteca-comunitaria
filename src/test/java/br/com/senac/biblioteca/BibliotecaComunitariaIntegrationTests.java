package br.com.senac.biblioteca;

import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request.CadastrarLivroRequest;
import br.com.senac.biblioteca.infrastructure.adapters.in.web.dto.request.RealizarEmprestimoRequest;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.EmprestimoJpaEntity;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.LeitorJpaEntity;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.entity.LivroJpaEntity;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository.SpringDataEmprestimoRepository;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository.SpringDataLeitorRepository;
import br.com.senac.biblioteca.infrastructure.adapters.out.persistence.repository.SpringDataLivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BibliotecaComunitariaIntegrationTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private SpringDataLivroRepository livroRepository;
    @Autowired private SpringDataLeitorRepository leitorRepository;
    @Autowired private SpringDataEmprestimoRepository emprestimoRepository;

    private LeitorJpaEntity leitorA;
    private LeitorJpaEntity leitorB;
    private LivroJpaEntity livroUnico;
    private LivroJpaEntity livroVarios;

    @BeforeEach
    void setUp() {
        emprestimoRepository.deleteAll();
        livroRepository.deleteAll();
        leitorRepository.deleteAll();
        leitorA = leitorRepository.save(new LeitorJpaEntity(null, "Lucas Teste"));
        leitorB = leitorRepository.save(new LeitorJpaEntity(null, "Mariana Teste"));
        livroUnico = livroRepository.save(new LivroJpaEntity(null, "Livro Raro", "Autor X", "978-0000000001", 1));
        livroVarios = livroRepository.save(new LivroJpaEntity(null, "Java Moderno", "Especialista", "978-0000000002", 5));
    }

    @Test
    @DisplayName("CA1: Livro com 1 exemplar ja emprestado -> novo emprestimo retorna 409 Conflict")
    void deveRetornar409QuandoLivroSemExemplarDisponivel() throws Exception {
        var req1 = new RealizarEmprestimoRequest(livroUnico.getId(), leitorA.getId());
        mockMvc.perform(post("/api/v1/emprestimos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.status", is("ATIVO")));

        var req2 = new RealizarEmprestimoRequest(livroUnico.getId(), leitorB.getId());
        mockMvc.perform(post("/api/v1/emprestimos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.detail", containsString("Nao ha exemplares disponiveis")));
    }

    @Test
    @DisplayName("CA2: Leitor com 2 emprestimos ativos -> 3º emprestimo retorna 409 Conflict")
    void deveRetornar409QuandoLeitorAtingirLimiteDeDoisEmprestimos() throws Exception {
        LivroJpaEntity l3 = livroRepository.save(new LivroJpaEntity(null, "L3", "A3", "978-0000000003", 2));
        LivroJpaEntity l4 = livroRepository.save(new LivroJpaEntity(null, "L4", "A4", "978-0000000004", 2));

        mockMvc.perform(post("/api/v1/emprestimos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new RealizarEmprestimoRequest(livroVarios.getId(), leitorA.getId())))).andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/emprestimos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new RealizarEmprestimoRequest(l3.getId(), leitorA.getId())))).andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/emprestimos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new RealizarEmprestimoRequest(l4.getId(), leitorA.getId()))))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.detail", containsString("limite maximo de 2")));
    }

    @Test
    @DisplayName("CA3: Devolver -> disponiveis aumentam em 1. Devolver de novo -> 409 Conflict")
    void deveAumentarDisponiveisERejeitarSegundaDevolucao() throws Exception {
        String resJson = mockMvc.perform(post("/api/v1/emprestimos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new RealizarEmprestimoRequest(livroUnico.getId(), leitorA.getId()))))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(resJson).get("id").asLong();

        mockMvc.perform(get("/api/v1/livros?busca=Raro")).andExpect(status().isOk()).andExpect(jsonPath("$[0].disponiveis", is(0)));
        mockMvc.perform(post("/api/v1/emprestimos/" + id + "/devolucao")).andExpect(status().isOk()).andExpect(jsonPath("$.status", is("DEVOLVIDO")));
        mockMvc.perform(get("/api/v1/livros?busca=Raro")).andExpect(status().isOk()).andExpect(jsonPath("$[0].disponiveis", is(1)));
        mockMvc.perform(post("/api/v1/emprestimos/" + id + "/devolucao")).andExpect(status().isConflict()).andExpect(jsonPath("$.detail", containsString("ja foi devolvido")));
    }

    @Test
    @DisplayName("CA4: Previsao para ontem e nao devolvido aparece em atrasados; devolvido nao aparece")
    void deveListarApenasEmprestimosAtrasadosENaoDevolvidos() throws Exception {
        LocalDate hoje = LocalDate.now();
        emprestimoRepository.save(new EmprestimoJpaEntity(null, livroUnico, leitorA, hoje.minusDays(15), hoje.minusDays(1), null, 0));
        emprestimoRepository.save(new EmprestimoJpaEntity(null, livroVarios, leitorB, hoje.minusDays(15), hoje.minusDays(1), hoje.minusDays(1), 0));

        mockMvc.perform(get("/api/v1/emprestimos/atrasados")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].livroTitulo", is("Livro Raro")));
    }

    @Test
    @DisplayName("CA5: ISBN repetido -> retorna 409 Conflict")
    void deveRetornar409QuandoIsbnDuplicado() throws Exception {
        var req = new CadastrarLivroRequest("Repetido", "Autor", livroUnico.getIsbn(), 2);
        mockMvc.perform(post("/api/v1/livros").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.detail", containsString("Ja existe um livro")));
    }

    @Test
    @DisplayName("Regra Central: Prazo de devolucao previsto = data emprestimo + 14 dias")
    void deveCalcularPrevisaoDevolucao14Dias() throws Exception {
        var req = new RealizarEmprestimoRequest(livroVarios.getId(), leitorA.getId());
        mockMvc.perform(post("/api/v1/emprestimos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.previstoPara", is(LocalDate.now().plusDays(14).toString())));
    }

    @Test
    @DisplayName("OpenAPI: Endpoint /v3/api-docs deve retornar 200 OK com definicao JSON")
    void deveRetornar200OpenApiDocs() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", notNullValue()));
    }

    @Test
    @DisplayName("Relatorios e rotas amigaveis: Endpoints de ranking e busca devem retornar 200 OK")
    void deveRetornar200RelatoriosEBusca() throws Exception {
        mockMvc.perform(get("/api/v1/livros/busca")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/relatorios/ranking-livros")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/relatorios/livros-mais-emprestados")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/relatorios/atrasados")).andExpect(status().isOk());
    }
}
