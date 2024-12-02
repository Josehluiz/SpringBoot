package com.example.sistema.controller;

import com.example.sistema.model.Pedido;
import com.example.sistema.repositories.PedidoRepositorio;
import com.example.sistema.repositories.ClienteRepositorio;
import com.example.sistema.repositories.ProdutoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class PedidoControle {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @GetMapping("/cadastroPedido")
    public ModelAndView cadastrar(Pedido pedido) {
        ModelAndView mv = new ModelAndView("administrativo/pedidos/cadastro");
        mv.addObject("pedido", pedido);
        mv.addObject("listaClientes", clienteRepositorio.findAll());
        mv.addObject("listaProdutos", produtoRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarPedido/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Pedido> pedido = pedidoRepositorio.findById(id);
        return cadastrar(pedido.orElse(new Pedido()));
    }

    @GetMapping("/removerPedido/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        pedidoRepositorio.delete(id);
        return listar();
    }

    @GetMapping("/listarPedido")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/pedidos/lista");
        mv.addObject("listaPedidos", pedidoRepositorio.findAll());
        return mv;
    }

    @PostMapping("/salvarPedido")
    public ModelAndView salvar(Pedido pedido, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(pedido);
        }
        pedidoRepositorio.save(pedido);
        return cadastrar(new Pedido());
    }
}