package com.infnet.pb.crud.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProdutoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        // Inicializa o MockMvc com o contexto da aplicação para testes de integração web
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Deve retornar status OK e a view de listagem de produtos")
    void deveRetornarStatusOkParaListaProdutos() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(view().name("produto/lista"))
                .andExpect(model().attributeExists("produtos"));
    }

    @Test
    @DisplayName("Deve carregar o formulário de cadastro de novo produto")
    void deveRetornarFormularioDeNovoProduto() throws Exception {
        mockMvc.perform(get("/produtos/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("produto/form"))
                .andExpect(model().attributeExists("produto"));
    }

    @Test
    @DisplayName("Deve salvar produto válido via POST e redirecionar para a lista")
    void deveRedirecionarAposSalvarProduto() throws Exception {
        mockMvc.perform(post("/produtos/salvar")
                        .param("nome", "Monitor Gamer 144hz")
                        .param("descricao", "Monitor ultra wide com 1ms de resposta")
                        .param("preco", "1200.00")
                        .param("quantidadeEstoque", "15"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));
    }

    @Test
    @DisplayName("Deve retornar erro de validação ao tentar salvar produto inválido")
    void devePermanecerNoFormAoFalharValidacao() throws Exception {
        mockMvc.perform(post("/produtos/salvar")
                        .param("nome", "") // Nome vazio costuma ser erro de validação
                        .param("preco", "-50.00")
                        .param("quantidadeEstoque", "-10"))
                // Se o controller estiver validando, ele volta pro form (Status 200)
                // Se não estiver, ele vai redirecionar (Status 302)
                .andExpect(status().isOk())
                .andExpect(view().name("produto/form"));
    }
}