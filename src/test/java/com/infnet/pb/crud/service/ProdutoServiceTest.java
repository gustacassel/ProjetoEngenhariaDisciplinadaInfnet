package com.infnet.pb.crud.service;

import com.infnet.pb.exception.RecursoNaoEncontradoException;
import com.infnet.pb.exception.RegraNegocioException;
import com.infnet.pb.model.Produto;
import com.infnet.pb.repository.ProdutoRepository;
import com.infnet.pb.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProdutoServiceTest {

    @Autowired
    private ProdutoService service;

    @Autowired
    private ProdutoRepository repository;

    @Test
    @DisplayName("Deve salvar um produto válido")
    void deveSalvarComSucesso() {
        Produto p = new Produto(null, "Teclado Mecânico", "RGB Switch Blue", 250.0, 10);
        Produto salvo = service.salvar(p);

        assertNotNull(salvo.getId());
        assertEquals("Teclado Mecânico", salvo.getNome());
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodos() {
        service.salvar(new Produto(null, "Produto 1", "Desc 1", 10.0, 5));
        service.salvar(new Produto(null, "Produto 2", "Desc 2", 20.0, 10));

        List<Produto> lista = service.listarTodos();
        assertTrue(lista.size() >= 2);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar produto com preço inválido")
    void deveValidarPreco() {
        Produto p = new Produto(null, "Produto Errado", "Desc", -10.0, 10);

        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> {
            service.salvar(p);
        });
        assertEquals("O preço do produto deve ser maior que zero.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve excluir um produto e validar exclusão")
    void deveExcluirProduto() {
        Produto salvo = service.salvar(new Produto(null, "Deletar", "Desc", 10.0, 1));
        Long id = salvo.getId();

        service.excluir(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(id));
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void deveLancarExcecaoIdInexistente() {
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.buscarPorId(999L);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "Ab", ""})
    @DisplayName("Deve validar nomes de produtos muito curtos")
    void deveValidarNomeCurto(String nomeCurto) {
        Produto p = new Produto(null, nomeCurto, "Desc", 100.0, 10);
        assertThrows(RegraNegocioException.class, () -> service.salvar(p));
    }
}