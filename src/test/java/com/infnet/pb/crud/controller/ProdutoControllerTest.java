package com.infnet.pb.crud.controller;

import com.infnet.pb.model.Produto;
import com.infnet.pb.repository.ProdutoRepository; // Adicione este import
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

    @Autowired
    private ProdutoRepository repository;

    @BeforeEach
    void setup() {
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
                .andExpect(status().isOk())
                .andExpect(view().name("produto/form"));
    }

    @Test
    @DisplayName("Deve carregar formulário de edição com dados do produto")
    void deveCarregarFormEdicao() throws Exception {
        Produto p = repository.save(new Produto(null, "Editavel", "Desc", 10.0, 5));

        mockMvc.perform(get("/produtos/editar/" + p.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("produto/form"))
                .andExpect(model().attributeExists("produto"));
    }

    @Test
    @DisplayName("Deve excluir um produto e redirecionar")
    void deveExcluirProduto() throws Exception {
        Produto p = repository.save(new Produto(null, "Deletar", "Desc", 10.0, 5));

        mockMvc.perform(get("/produtos/excluir/" + p.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));
    }
}
