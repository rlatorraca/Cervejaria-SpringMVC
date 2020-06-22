package com.rlsp.cervejaria.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rlsp.cervejaria.controller.page.PageWrapper;
import com.rlsp.cervejaria.model.Cidade;
import com.rlsp.cervejaria.repository.CidadesRepository;
import com.rlsp.cervejaria.repository.EstadosRepository;
import com.rlsp.cervejaria.repository.filter.CidadeFilter;
import com.rlsp.cervejaria.service.CadastroCidadeService;
import com.rlsp.cervejaria.service.exception.ImpossivelExcluirEntidadeException;
import com.rlsp.cervejaria.service.exception.NomeCidadeJaCadastradaException;

@Controller
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadesRepository cidades;
	
	@Autowired
	private EstadosRepository estados;
	
	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@RequestMapping("/nova")
	public ModelAndView nova(Cidade cidade) {
		ModelAndView mv = new ModelAndView("cidade/CadastroCidade");
		mv.addObject("estados", estados.findAll());
		return mv;
	}
	
	/**
	 * Respondera a uma requisicao com GET onde CONTENT_TYPE = JSON
	 *  - @RequestParam(name="estado", defaultValue = "-1") ==> para poder receber o "estado" vindo da URL (cervejaria
	 *  - @ResponseBody ==> para fazer o RETORNO como arquivo JSON 
	 *  - @Cacheable("cidades") ==> fazer o Cache em Memoria
	 *  	 - key = "#codigoEstado" ==> Invaida o CACHE apenas para o codigo do estado enviado por @CacheEvict (ao salvar)
	 */
	//@Cacheable("cidades")
	@Cacheable(value = "cidades", key = "#codigoEstado")
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody List<Cidade> pesquisaCidadePorCodigoEstado (@RequestParam(name="estado", defaultValue = "-1") Long codigoEstado) {
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {	}
		
		return cidades.findByEstadoCodigo(codigoEstado);
	
	}
	
	/**
	 * @CacheEvict(value = "cidades", allEntries = true) ==> usado para APAGAR O CACHE em caso de NOVO cidades adicionada
	 *  - allEntries = true ==> invalidade TODO CACHE 
	 *  - key="#cidade.estado.codigo" ==> pega o CODIDO do ESTADO
	 *  - condition="#cidade.temEstado()" ==> se TRUE sera apagado o CACHE
	 */
	//@CacheEvict(value = "cidades", allEntries = true)
	
	@PostMapping({"/nova", "/nova/{codigo}"})
	@CacheEvict(value = "cidades", key="#cidade.estado.codigo", condition="#cidade.temEstado()")
	public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			
			return nova(cidade);
			
		}
		
		try {
			cadastroCidadeService.salvar(cidade);
		} catch (NomeCidadeJaCadastradaException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return nova(cidade);
		}
		
		attributes.addFlashAttribute("mensagem", "Cidade salva com sucesso!");
		return new ModelAndView("redirect:/cidades/nova");
	}
	
	@GetMapping
	public ModelAndView pesquisar(CidadeFilter cidadeFilter, BindingResult result
			, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cidade/PesquisaCidades");
		mv.addObject("estados", estados.findAll());
		
		PageWrapper<Cidade> paginaWrapper = new PageWrapper<>(cidades.filtrar(cidadeFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> excluir(@PathVariable("codigo") Cidade cidade) {
		try {
			this.cadastroCidadeService.excluir(cidade);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView atualizar(@PathVariable("codigo") Cidade cidade) {
		ModelAndView mv = this.nova(cidade);
		mv.addObject(this.cidades.findByCodigoFetchingEstado(cidade.getCodigo()));
		return mv;
	}
	
}
