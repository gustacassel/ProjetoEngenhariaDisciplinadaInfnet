package com.infnet.pb.crud.service;

import com.infnet.pb.exception.RecursoNaoEncontradoException;
import com.infnet.pb.exception.RegraNegocioException;
import com.infnet.pb.model.Cliente;
import com.infnet.pb.repository.ClienteRepository;
import com.infnet.pb.service.ClienteService;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClienteServiceTest {

    @Autowired
    private ClienteService service;

    @Autowired
    private ClienteRepository repository;

    @Test
    @DisplayName("Deve salvar um cliente válido")
    void deveSalvarClienteComSucesso() {
        Cliente c = new Cliente(null, "Gustavo Teste", "gustavo@email.com", LocalDate.now().minusYears(20), 1500.0);
        Cliente salvo = service.salvar(c);

        assertNotNull(salvo.getId());
        assertEquals("Gustavo Teste", salvo.getNome());
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    void deveListarTodosOsClientes() {
        service.salvar(new Cliente(null, "Cliente 1", "c1@mail.com", LocalDate.now().minusYears(30), 100.0));
        service.salvar(new Cliente(null, "Cliente 2", "c2@mail.com", LocalDate.now().minusYears(25), 200.0));

        List<Cliente> lista = service.listarTodos();
        assertTrue(lista.size() >= 2);
    }

    @Test
    @DisplayName("Deve buscar cliente por ID")
    void deveBuscarPorId() {
        Cliente salvo = service.salvar(new Cliente(null, "Busca", "busca@mail.com", LocalDate.now().minusYears(20), 50.0));
        Cliente encontrado = service.buscarPorId(salvo.getId());

        assertNotNull(encontrado);
        assertEquals(salvo.getId(), encontrado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente (Fail Gracefully)")
    void deveLancarExcecaoBuscaInexistente() {
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.buscarPorId(999L);
        });
    }

    @Test
    @DisplayName("Deve excluir um cliente")
    void deveExcluirCliente() {
        Cliente salvo = service.salvar(new Cliente(null, "Excluir", "ex@mail.com", LocalDate.now().minusYears(20), 0.0));
        Long id = salvo.getId();

        service.excluir(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(id));
    }

    @Test
    @DisplayName("Não deve permitir e-mails duplicados (Regra de Negócio)")
    void deveValidarEmailDuplicado() {
        String email = "duplicado@email.com";
        service.salvar(new Cliente(null, "Primeiro", email, LocalDate.now().minusYears(20), 100.0));

        Cliente segundo = new Cliente(null, "Segundo", email, LocalDate.now().minusYears(20), 200.0);

        assertThrows(RegraNegocioException.class, () -> {
            service.salvar(segundo);
        });
    }

    @Property
    void fuzzingSalvar(@ForAll @AlphaChars @StringLength(min = 1, max = 200) String nome) {
        try {
            Cliente c = new Cliente(null, nome, "fuzz@mail.com", LocalDate.now().minusYears(10), 10.0);
            service.salvar(c);
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    void deveLancarExcecaoAoBuscarClienteInexistente() {
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.buscarPorId(999L); // ID que não existe
        });
    }

    @Test
    void deveLancarExcecaoAoExcluirInexistente() {
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.excluir(999L);
        });
    }
}