package com.rlsp.cervejaria.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rlsp.cervejaria.controller.page.PageWrapper;
import com.rlsp.cervejaria.dto.CervejaDTO;
import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.Origem;
import com.rlsp.cervejaria.model.Sabor;
import com.rlsp.cervejaria.repository.CervejasRepository;
import com.rlsp.cervejaria.repository.EstilosRepository;
import com.rlsp.cervejaria.repository.filter.CervejaFilter;
import com.rlsp.cervejaria.service.CadastroCervejaService;
import com.rlsp.cervejaria.service.exception.ImpossivelExcluirEntidadeException;


/**
 * Classe CONTROLLER para as cervejas a serem criadas / manipuladas.
 * @author Rodrigo
 *
 */
@Controller
@RequestMapping("/cervejas")
public class BeerController{
		
	@Autowired
	private EstilosRepository estilos;
	
	@Autowired
	private CadastroCervejaService cadastroCervejaService; 
	
	@Autowired
	private CervejasRepository cervejas;

	@RequestMapping("/nova")
	public ModelAndView nova(Cerveja beer) {
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
	 * "/nova" , "\\d+" ==> servira tanto para "/nova" (NOVA CERVEJA), quanto para "qualquer NUMERO" (EDITAR)
	 *  = \\d+ ==> todos digitos 0 a infinito
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/nova" , "{\\d+}"}, method = RequestMethod.POST)
	public ModelAndView registerBeer(@Valid Cerveja beer, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		
		
		if (result.hasErrors()) {
			//model.addAttribute(beer);
			//model.addAttribute("message", "Existem Erros");
			
			return nova(beer);
		}		
		
		cadastroCervejaService.salvar(beer);		
		redirectAttributes.addFlashAttribute("mensagem", "CERVEJA salva com SUCESSO !!!!"); // Existirão mesmo APÓS um REDIRECT
		
		return new ModelAndView("redirect:/cervejas/nova");
	}
	
	
	/**
	 * Faz a pesquisa de Cervejas
	 * Pageable ==> objeto para fazer a paginacao
	 *  @PageableDefault(size = 2) ==> o numero de linhas por pagina
	 */
	@GetMapping
	public ModelAndView pesquisar(CervejaFilter cervejaFilter, BindingResult result
			, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cerveja/PesquisaCervejas");
		mv.addObject("estilos", estilos.findAll());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());
		
		PageWrapper<Cerveja> paginaWrapper = new PageWrapper<>(cervejas.filtrar(cervejaFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	/**
	 * @ResponseBody ==> retornar um OBJETO JSON	 * 
	 */
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<CervejaDTO> pesquisar(String skuOuNome) {
		return cervejas.porSkuOuNome(skuOuNome);
	}
	
	
	/**
	 * @ResponseBody ResponseEntity<?> ==> retorna uma STRING (ERRO ou SUCESSO)	 * 
	 */
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Cerveja cerveja) {
		try {
			cadastroCervejaService.excluir(cerveja);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage()); // Retorna o e.getMessage pra ser usado no JS para mostrar a mensagem de ERRO (excecao) na tela
		}
		return ResponseEntity.ok().build();
	}
	
	

	/**
	 * Serve para EDITAR a cerveja	 * 
	 */
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Cerveja cerveja) {
		ModelAndView mv = nova(cerveja);
		mv.addObject(cerveja); // Adiciona as informacoes da CERVEJA e chama a pagina de cadastro da CERVEJA (mudando para Editar)
		return mv;
	}
}
