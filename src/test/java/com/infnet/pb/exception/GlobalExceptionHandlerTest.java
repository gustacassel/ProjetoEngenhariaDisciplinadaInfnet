package com.infnet.pb.exception;

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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ProdutoService produtoService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Deve capturar Exception genérica e retornar view 500")
    void deveTratarErroGenerico() throws Exception {
        // Simula um erro inesperado (ex: NullPointerException ou erro de banco)
        Mockito.when(produtoService.listarTodos())
                .thenThrow(new RuntimeException("Falha crítica no servidor"));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("technicalDetail", "RuntimeException"));
    }
}
