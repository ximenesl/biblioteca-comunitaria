package br.com.senac.biblioteca.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Biblioteca Comunitária — API REST")
                        .description("""
                                API de gestão de acervo e empréstimos da Biblioteca Comunitária.
                                
                                Permite cadastrar livros, realizar e devolver empréstimos,
                                consultar histórico de leitores e gerar relatórios de livros
                                mais emprestados e empréstimos em atraso.
                                """)
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Senac — Projeto Biblioteca Comunitária")
                                .email("contato@biblioteca.senac.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local de desenvolvimento")));
    }
}