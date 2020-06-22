package com.rlsp.cervejaria.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rlsp.cervejaria.controller.page.PageWrapper;
import com.rlsp.cervejaria.model.Estilo;
import com.rlsp.cervejaria.repository.EstilosRepository;
import com.rlsp.cervejaria.repository.filter.EstiloFilter;
import com.rlsp.cervejaria.service.CadastroEstiloService;
import com.rlsp.cervejaria.service.exception.ImpossivelExcluirEntidadeException;
import com.rlsp.cervejaria.service.exception.NomeEstiloJaCadastradoException;

@Controller
@RequestMapping("/estilos")
public class EstiloController {

	@Autowired
	private CadastroEstiloService cadastroEstiloService;
	
	@Autowired
	private EstilosRepository estilos;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Estilo estilo) {
		ModelAndView mv = new ModelAndView("estilo/CadastroEstilo");
		
		if(estilo.getCodigo() != null) {
			mv.addObject("nome", estilo.getNome() );
			System.out.println(" >> Dentro de : public ModelAndView novo(Estilo estilo) - " + estilo.getNome() + "/" + estilo.getCodigo());
		}
		
		return mv;
	}
	
	//@RequestMapping(value = "/novo", method = RequestMethod.POST)
	@RequestMapping(value = { "/novo", "{\\d+}" }, method = RequestMethod.POST)	
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(estilo);
		}
		
		try {
			cadastroEstiloService.salvar(estilo);
		} catch (NomeEstiloJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(estilo);
		}
		
		attributes.addFlashAttribute("mensagem", "Estilo salvo com sucesso");
		return new ModelAndView("redirect:/estilos/novo");
	}
	
	/**
	 * AJAX + JAVA
	 * consumes = { MediaType.APPLICATION_JSON_VALUE } ==> recebera um JSON 
	 * @RequestBody ==> para converter o JSON para objeto JAVA
	 * @ResponseBody ==> ira responder com um STATUS HTTP (diferente do padrao que um VIEW ou REDIRECT
	 * @ResponseEntity ==> retorn um STATUS do HTTP (400, 500, 200 ,201, etc) 
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
		}
		
		estilo = cadastroEstiloService.salvar(estilo);
		
		
//		try {
//			estilo = cadastroEstiloService.salvar(estilo);
//		} catch (NomeEstiloJaCadastradoException e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		
		return ResponseEntity.ok(estilo); //ResponseEntity.ok = HTTP=200
	}
	
	@GetMapping
	public ModelAndView pesquisar(EstiloFilter estiloFilter, BindingResult result
			, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("estilo/PesquisaEstilos");
		
		PageWrapper<Estilo> paginaWrapper = new PageWrapper<>(estilos.filtrar(estiloFilter, pageable), httpServletRequest);			
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Long codigo) {
		ModelAndView modelAndView = new ModelAndView("estilo/CadastroEstilo");
		Estilo estilo = estilos.findByCodigo(codigo);
		modelAndView.addObject(estilo);
		return modelAndView;
	}
	

	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> excluir(@PathVariable("codigo") Estilo estilo) {
		try {
			this.cadastroEstiloService.excluir(estilo);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
}