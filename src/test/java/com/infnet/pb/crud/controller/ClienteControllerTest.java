package com.infnet.pb.crud.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ClienteControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void deveRetornarStatusOkParaListaClientes() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/lista")); // Adicionado 'cliente/'
    }

    @Test
    void deveRetornarFormularioDeNovoCliente() throws Exception {
        mockMvc.perform(get("/clientes/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/form")); // Ajuste o nome se for outro
    }

    @Test
    void deveRedirecionarAposSalvar() throws Exception {
        // Simula o envio de um formulário (POST)
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/clientes/salvar")
                        .param("nome", "Fulano de Tal")
                        .param("email", "fulano@teste.com")
                        .param("dataNascimento", "2000-01-01")
                        .param("limiteCredito", "500.00"))
                .andExpect(status().is3xxRedirection()) // Espera um redirect (302)
                .andExpect(redirectedUrl("/clientes"));
    }
}