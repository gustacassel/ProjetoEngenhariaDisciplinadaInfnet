package com.infnet.pb.controller;

import com.infnet.pb.service.ClienteService;
import com.infnet.pb.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalClientes", clienteService.listarTodos().size());
        model.addAttribute("totalProdutos", produtoService.listarTodos().size());
        return "home";
    }
}
