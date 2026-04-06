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

@Controller
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", service.listarTodos());
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
                         RedirectAttributes ra,
                         Model model) {

        if (result.hasErrors()) {
            return "produto/form";
        }

        try {
            service.salvar(produto);
            ra.addFlashAttribute("mensagemSucesso", "Produto salvo com sucesso!");
            return "redirect:/produtos";
        } catch (RegraNegocioException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            return "produto/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("produto", service.buscarPorId(id));
        return "produto/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        service.excluir(id);
        ra.addFlashAttribute("mensagemSucesso", "Produto removido com sucesso!");
        return "redirect:/produtos";
    }
}
