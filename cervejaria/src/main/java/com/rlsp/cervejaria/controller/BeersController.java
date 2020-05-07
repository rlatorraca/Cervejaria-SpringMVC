package com.rlsp.cervejaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rlsp.cervejaria.model.Beer;

import jakarta.validation.Valid;

/**
 * Classe CONTROLLER para as cervejas a serem criadas / manipuladas.
 * @author Rodrigo
 *
 */
@Controller
public class BeersController {

	@RequestMapping("/cervejas/newbeer")
	public String newBeer(Beer beer) {
		// model.addAttribute(new Beer()); //cria e passa o OBJETO de "Beer" para o HTML
		return "cervejas/registerbeer.html";
	}
	
	/**
	 * @Valid ==> serve para validar o Objeto cerveja
	 * O resultado do @Valid é entregue no Objet BindingResult "results"
	 * @param cerveja
	 * @param result ==> recebe o resultado da validação dos campos feita pelo SPRING
	 * @param model ==> passagem INFORMAÇÕES/MENSAGENS para a página  HTML, quando fizer o "FORWARD"
	 * @param redirectAttributes ==> passagem INFORMACÔES/MENSAGENS para a p�gina  HTML, quando fizer o "REDIRECT"
	 * 
	 * Forward (Padrão do Spring)==> faz um REQUEST, retorna o 200 (OK) e uma RESPONSE (geralmente a pagina html)
	 * Redirect ==> apresenta uma LOCATION para uma "URL" (faz uma "GET").
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cervejas/newbeer", method = RequestMethod.POST)
	public String registerBeer(@Valid Beer beer, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			model.addAttribute(beer);
			
			return newBeer(beer);
		}
		redirectAttributes.addFlashAttribute("mensagem", "Formulário preenchido com SUCESSO !!!!"); // Existir� mesmo AP�S um REDIRECT
		System.out.println(">>> sku: " + beer.getSku());
		return "redirect:/cervejas/newbeer";
	}
}
