package com.rlsp.cervejaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rlsp.cervejaria.repository.CervejasRepository;
import com.rlsp.cervejaria.repository.ClientesRepository;
import com.rlsp.cervejaria.repository.VendasRepository;



@Controller
public class DashboardController {

	@Autowired
	private VendasRepository vendas;
	
	@Autowired
	private CervejasRepository cervejas;
	
	@Autowired
	private ClientesRepository clientes;
	
	@GetMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("Dashboard");
		
		mv.addObject("vendasNoAno", vendas.valorTotalNoAno());
		mv.addObject("vendasNoMes", vendas.valorTotalNoMes());
		mv.addObject("ticketMedio", vendas.valorTicketMedioNoAno());
		
		mv.addObject("valorItensEstoque", cervejas.valorItensEstoque());
		mv.addObject("totalClientes", clientes.count());
		
		return mv;
	}
	
}