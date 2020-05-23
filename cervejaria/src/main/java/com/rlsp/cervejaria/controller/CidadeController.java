package com.rlsp.cervejaria.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlsp.cervejaria.model.Cidade;
import com.rlsp.cervejaria.repository.CidadesRepository;

@Controller
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadesRepository cidades;
	
	@RequestMapping("nova")
	public String nova() {
		return "cidade/CadastroCidade";
	}
	
	/**
	 * Respondera a uma requisicao com GET onde CONTENT_TYPE = JSON
	 *  - @RequestParam(name="estado", defaultValue = "-1") ==> para poder receber o "estado" vindo da URL (cervejaria
	 *  - @ResponseBody ==> para fazer o RETORNO como arquivo JSON 
	 */
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public @ResponseBody List<Cidade> pesquisaCidadePorCodigoEstado (@RequestParam(name="estado", defaultValue = "-1") Long codigoEstado) {
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {	}
		
		return cidades.findByEstadoCodigo(codigoEstado);
	
	}
	
//	@PostMapping("/nova")
//	public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attributes) {
//		if (result.hasErrors()) {
//			return nova(cidade);
//		}
//		
//		try {
//			cadastroCidadeService.salvar(cidade);
//		} catch (NomeCidadeJaCadastradaException e) {
//			result.rejectValue("nome", e.getMessage(), e.getMessage());
//			return nova(cidade);
//		}
//		
//		attributes.addFlashAttribute("mensagem", "Cidade salva com sucesso!");
//		return new ModelAndView("redirect:/cidades/nova");
//	}
//	
//	@GetMapping
//	public ModelAndView pesquisar(CidadeFilter cidadeFilter, BindingResult result
//			, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
//		ModelAndView mv = new ModelAndView("cidade/PesquisaCidades");
//		mv.addObject("estados", estados.findAll());
//		
//		PageWrapper<Cidade> paginaWrapper = new PageWrapper<>(cidades.filtrar(cidadeFilter, pageable)
//				, httpServletRequest);
//		mv.addObject("pagina", paginaWrapper);
//		return mv;
//	}
	
	
}
