package com.rlsp.cervejaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rlsp.cervejaria.model.TipoPessoa;
import com.rlsp.cervejaria.repository.EstadosRepository;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private EstadosRepository estados;
	
	@RequestMapping("novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estados.findAll());
		return mv;
	}
	
}
