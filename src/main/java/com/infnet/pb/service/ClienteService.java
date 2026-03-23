package com.infnet.pb.service;

import com.infnet.pb.exception.RecursoNaoEncontradoException;
import com.infnet.pb.exception.RegraNegocioException;
import com.infnet.pb.model.Cliente;
import com.infnet.pb.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Injeção de dependência via construtor (Best Practice)
public class ClienteService {

    private final ClienteRepository repository;

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id));
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        // Fail Early: Verificar se o e-mail já existe em OUTRO cliente
        verificarEmailDuplicado(cliente);
        return repository.save(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Não é possível excluir. Cliente não encontrado.");
        }
        repository.deleteById(id);
    }

    // Método auxiliar para validação (Clean Code)
    private void verificarEmailDuplicado(Cliente cliente) {
        Optional<Cliente> clienteExistente = repository.findByEmail(cliente.getEmail());

        if (clienteExistente.isPresent()) {
            // Se for edição (tem ID), verifica se o e-mail pertence a outra pessoa
            if (cliente.getId() != null && !clienteExistente.get().getId().equals(cliente.getId())) {
                throw new RegraNegocioException("O e-mail informado já pertence a outro cliente.");
            }
            // Se for criação (sem ID), falha direto
            if (cliente.getId() == null) {
                throw new RegraNegocioException("Já existe um cliente cadastrado com este e-mail.");
            }
        }
    }
}