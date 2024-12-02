package com.example.sistema.controller;

import com.example.sistema.model.Cliente;
import com.example.sistema.repositories.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ClienteControle {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @GetMapping("/cadastroCliente")
    public ModelAndView cadastrar(Cliente cliente) {
        ModelAndView mv = new ModelAndView("administrativo/clientes/cadastro");
        mv.addObject("cliente", cliente);
        return mv;
    }

    @GetMapping("/editarCliente/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = clienteRepositorio.findById(id);
        return cadastrar(cliente.orElse(new Cliente()));
    }

    @GetMapping("/removerCliente/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        clienteRepositorio.delete(id);
        return listar();
    }

    @GetMapping("/listarCliente")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/clientes/lista");
        mv.addObject("listaClientes", clienteRepositorio.findAll());
        return mv;
    }

    @PostMapping("/salvarCliente")
    public ModelAndView salvar(Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(cliente);
        }
        clienteRepositorio.save(cliente);
        return cadastrar(new Cliente());
    }
}