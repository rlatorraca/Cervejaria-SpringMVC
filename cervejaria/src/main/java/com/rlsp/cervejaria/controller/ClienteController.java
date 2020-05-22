package com.rlsp.cervejaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rlsp.cervejaria.model.TipoPessoa;

@Controller
@RequestMapping("/clientes")
public class ClienteController {


	
	@RequestMapping("novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		mv.addObject("tiposPessoa", TipoPessoa.values()); // CPF ou CNPJ (PEssoa fiscia ou juridica
		return mv;
	}
}
