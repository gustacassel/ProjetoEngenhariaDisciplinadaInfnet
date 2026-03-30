package com.infnet.pb.service;

import com.infnet.pb.exception.RecursoNaoEncontradoException;
import com.infnet.pb.exception.RegraNegocioException;
import com.infnet.pb.model.Produto;
import com.infnet.pb.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository repository;

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com ID: " + id));
    }

    public Produto salvar(Produto produto) {
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new RegraNegocioException("O preço do produto deve ser maior que zero.");
        }

        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            throw new RegraNegocioException("A quantidade em estoque não pode ser negativa.");
        }

        if (produto.getNome() == null || produto.getNome().trim().length() < 3) {
            throw new RegraNegocioException("O nome do produto deve ter pelo menos 3 caracteres.");
        }

        return repository.save(produto);
    }

    public void excluir(Long id) {
        Produto produto = buscarPorId(id);
        repository.delete(produto);
    }
}
