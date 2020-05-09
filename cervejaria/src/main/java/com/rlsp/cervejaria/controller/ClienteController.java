package com.rlsp.cervejaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rlsp.cervejaria.model.Cliente;

@Controller
public class ClienteController {

	@RequestMapping("/clientes/novo")
	public String novoCliente(Cliente cliente) {
		// model.addAttribute(new Beer()); //cria e passa o OBJETO de "Beer" para o HTML
		return "cliente/CadastroCliente.html";
	}
}
