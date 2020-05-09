package com.rlsp.cervejaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rlsp.cervejaria.model.Usuario;

@Controller
public class UsuarioController {
	
	@RequestMapping("/usuarios/novo")
	public String novoUsuario(Usuario usuario) {
		// model.addAttribute(new Beer()); //cria e passa o OBJETO de "Beer" para o HTML
		return "usuario/CadastroUsuario.html";
	}

}
