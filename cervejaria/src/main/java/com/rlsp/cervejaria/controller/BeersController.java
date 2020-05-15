package com.rlsp.cervejaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.Origem;
import com.rlsp.cervejaria.model.Sabor;
import com.rlsp.cervejaria.repository.CervejasRepository;
import com.rlsp.cervejaria.repository.EstilosRepository;
import com.rlsp.cervejaria.service.CadastroCervejaService;

import jakarta.validation.Valid;

/**
 * Classe CONTROLLER para as cervejas a serem criadas / manipuladas.
 * @author Rodrigo
 *
 */
@Controller
public class BeersController {
	


	
	@Autowired
	private EstilosRepository estilos;
	
	@Autowired
	private CadastroCervejaService cadastroCervejaService; 
	

	@RequestMapping("/cervejas/newbeer")
	public ModelAndView newBeer(Cerveja beer) {
		ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja");
		mv.addObject("sabores", Sabor.values());
		mv.addObject("estilos", estilos.findAll());
		mv.addObject("origens", Origem.values());
		//cervejas.findAll();
		// model.addAttribute(new Beer()); //cria e passa o OBJETO de "Beer" para o HTML
		return mv;
	}
	
	/**
	 * @Valid ==> serve para validar o Objeto cerveja
	 * O resultado do @Valid é entregue no Objet BindingResult "results"
	 * @param cerveja
	 * @param result ==> recebe o resultado da validação dos campos feita pelo SPRING
	 * @param model ==> passagem INFORMAÇÕES/MENSAGENS para a página  HTML, quando fizer o "FORWARD"
	 * @param redirectAttributes ==> passagem INFORMACÔES/MENSAGENS para a página  HTML, quando fizer o "REDIRECT"
	 * 
	 * -----------------
	 * 
	 * >>> Forward (Padrão do Spring)==> faz um REQUEST (faz um GET), retorna o 200 (OK) e uma RESPONSE (geralmente a pagina html / uma VIEW)
	 * 
	 * >>> Redirect ==> REQUEST (faz um POST), qure RESPONDE com um 302 (FOUND) que apresenta uma LOCATION (url) para uma "URL"  e faz uma "GET" na LOCATION (url)
	 * 	- Objetivo: comecar /renovar as especificacoes no local de destino
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cervejas/newbeer", method = RequestMethod.POST)
	public ModelAndView registerBeer(@Valid Cerveja beer, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		
		System.out.println(">>> sku: " + beer.getSku());
		System.out.println(">>> Sabor: " + beer.getSabor());
		System.out.println(">>> Estilo: " + beer.getEstilo());
		System.out.println(">>> Origem: " + beer.getOrigem());
		System.out.println(">>> Nome: " + beer.getNome());
		System.out.println(">>> Descricao: " + beer.getDescricao());
		System.out.println(">>> Estoque: " + beer.getQuantidadeEstoque());
		System.out.println(">>> Comissao: " + beer.getComissao());
		System.out.println(">>> Codigo: " + beer.getCodigo());
		System.out.println(">>> Teor: " + beer.getTeorAlcoolico());
		System.out.println(">>> Valor: " + beer.getValor());
		
		
		if (result.hasErrors()) {
			//model.addAttribute(beer);
			//model.addAttribute("message", "Existem Erros");
			System.out.println("ERROR");
			return newBeer(beer);
		}		
		
		cadastroCervejaService.salvar(beer);
		redirectAttributes.addFlashAttribute("mensagem", "Formulário preenchido com SUCESSO !!!!"); // Existirão mesmo APÓS um REDIRECT
		System.out.println(">>> sku: " + beer.getSku());
		System.out.println(">>> Sabor: " + beer.getSabor());
		System.out.println(">>> Estilo: " + beer.getEstilo());
		System.out.println(">>> Origem: " + beer.getOrigem());
		return new ModelAndView("redirect:/cervejas/newbeer");
	}
	
	
	
}
