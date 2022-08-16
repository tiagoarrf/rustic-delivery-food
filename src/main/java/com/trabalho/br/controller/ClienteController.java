package com.trabalho.br.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trabalho.br.model.Cliente;
import com.trabalho.br.service.ClienteService;

@Controller
public class ClienteController {

    List<Cliente> clientes = new ArrayList<Cliente>();

    @Autowired
    private ClienteService clienteService;

    @RequestMapping("/entrar")
    public ModelAndView logar() {
        ModelAndView mv = new ModelAndView("entrar");
        mv.addObject("cliente", new Cliente());
        return mv;
    }

    @RequestMapping("/cliente/cadastrar")
    public ModelAndView cadastrar() {
        ModelAndView mv = new ModelAndView("cliente/cadastrar_cliente");
        mv.addObject("cliente", new Cliente());
        return mv;
    }

    @PostMapping("/cliente/salvar")
    public ModelAndView salvar(@Validated Cliente cliente, BindingResult result) {
        return clienteService.save(cliente, result);
    }

    @GetMapping("/gerente/cliente/listar")
    public ModelAndView listar() {
        List<Cliente> listaClientes = clienteService.listClientes();
        ModelAndView mv = new ModelAndView("cliente/listar_clientes");
        mv.addObject("listaClientes", listaClientes);

        return mv;
    }

    @RequestMapping("/cliente/excluir/{id}")
    public ModelAndView excluir(@PathVariable Long id) {
        clienteService.delete(id);
        ModelAndView mv = new ModelAndView("redirect:/prato/listar_cliente");
        return mv;
    }

    @RequestMapping("/cliente/atualizar")
    public ModelAndView atualizar() {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = (UserDetails) auth;
        Cliente cliente = clienteService.buscarEmail(user.getUsername());
        ModelAndView mv = new ModelAndView("cliente/atualizar_cliente");
        mv.addObject("cliente", cliente);
        return mv;
    }

    @PostMapping("/cliente/atualizar")
    public ModelAndView atualizar(@Validated Cliente cliente, BindingResult result) {
        return clienteService.update(cliente, result);
    }

}
