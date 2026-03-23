package com.infnet.pb.controller;

import com.infnet.pb.exception.RegraNegocioException;
import com.infnet.pb.model.Cliente;
import com.infnet.pb.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", service.listarTodos());
        return "cliente/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("cliente") Cliente cliente,
                         BindingResult result,
                         RedirectAttributes attributes,
                         Model model) {

        // Fail Early: Erros de validação de campo (ex: email inválido)
        if (result.hasErrors()) {
            return "cliente/form";
        }

        try {
            service.salvar(cliente);
            attributes.addFlashAttribute("mensagemSucesso", "Cliente salvo com sucesso!");
            return "redirect:/clientes";
        } catch (RegraNegocioException e) {
            // Fail Gracefully: Erro de negócio (ex: email duplicado) volta para o form com mensagem
            model.addAttribute("erroNegocio", e.getMessage());
            return "cliente/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", service.buscarPorId(id));
        return "cliente/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        service.excluir(id);
        attributes.addFlashAttribute("mensagemSucesso", "Cliente removido com sucesso!");
        return "redirect:/clientes";
    }
}