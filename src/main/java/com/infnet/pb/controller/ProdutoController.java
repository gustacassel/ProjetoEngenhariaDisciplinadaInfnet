package com.infnet.pb.controller;

import com.infnet.pb.exception.RegraNegocioException;
import com.infnet.pb.model.Produto;
import com.infnet.pb.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService service;

    @GetMapping
    public String listar(Model model) {
        List<Produto> produtos = service.listarTodos();
        model.addAttribute("produtos", produtos);
        return "produto/lista";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        if (!model.containsAttribute("produto")) {
            model.addAttribute("produto", new Produto());
        }
        return "produto/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("produto") Produto produto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "produto/form";
        }

        try {
            service.salvar(produto);
            ra.addFlashAttribute("mensagemSucesso", "Produto salvo com sucesso!");
            return "redirect:/produtos";
        } catch (RegraNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            return "produto/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Produto produto = service.buscarPorId(id);
            model.addAttribute("produto", produto);
            return "produto/form";
        } catch (Exception e) {
            return "redirect:/produtos";
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("mensagemSucesso", "Produto excluído com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao excluir produto.");
        }
        return "redirect:/produtos";
    }
}
