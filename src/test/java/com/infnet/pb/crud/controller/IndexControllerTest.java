package com.infnet.pb.crud.controller;

import com.infnet.pb.model.Cliente;
import com.infnet.pb.model.Produto;
import com.infnet.pb.service.ClienteService;
import com.infnet.pb.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class IndexControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private ProdutoService produtoService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Deve carregar a home com os totais de clientes e produtos")
    void deveCarregarIndexComTotais() throws Exception {
        // Mockando lista de clientes (simulando 2 clientes)
        Mockito.when(clienteService.listarTodos())
                .thenReturn(Arrays.asList(new Cliente(), new Cliente()));

        // Mockando lista de produtos (simulando 3 produtos)
        Mockito.when(produtoService.listarTodos())
                .thenReturn(Arrays.asList(new Produto(), new Produto(), new Produto()));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("totalClientes", 2))
                .andExpect(model().attribute("totalProdutos", 3));
    }

    @Test
    @DisplayName("Deve carregar a home mesmo com listas vazias")
    void deveCarregarIndexComListasVazias() throws Exception {
        Mockito.when(clienteService.listarTodos()).thenReturn(Collections.emptyList());
        Mockito.when(produtoService.listarTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalClientes", 0))
                .andExpect(model().attribute("totalProdutos", 0));
    }
}
