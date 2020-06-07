package com.rlsp.cervejaria.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.rlsp.cervejaria.controller.page.PageWrapper;
import com.rlsp.cervejaria.model.Cliente;
import com.rlsp.cervejaria.model.TipoPessoa;
import com.rlsp.cervejaria.repository.ClientesRepository;
import com.rlsp.cervejaria.repository.EstadosRepository;
import com.rlsp.cervejaria.repository.filter.ClienteFilter;
import com.rlsp.cervejaria.service.CadastroClienteService;
import com.rlsp.cervejaria.service.exception.CpfCnpjClienteJaCadastradoException;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private EstadosRepository estados;
	
	@Autowired
	private CadastroClienteService cadastroClienteService;
	
	@Autowired
	private ClientesRepository clientes;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estados.findAll());
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(cliente);
		}
		
		try {
			cadastroClienteService.salvar(cliente);
		} catch(CpfCnpjClienteJaCadastradoException e) {
			// Para fazer a VALIDACAO do CAMPO via JAVA (e nao JPA nas classes , ex.: @NotBlank)
			result.rejectValue("cpfOuCnpj", e.getMessage(), e.getMessage()); 
			return novo(cliente);
		}
		
		attributes.addFlashAttribute("mensagem", "CLIENTE salvo com sucesso!");
		return new ModelAndView("redirect:/clientes/novo");
	}
	

	@GetMapping
	public ModelAndView pesquisar(ClienteFilter clienteFilter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView mv = new ModelAndView("cliente/PesquisaClientes");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		
		PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(clientes.filtrar(clienteFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	/**
	 * Essa Requisicao GET sera usado pela Consulta Rapida de Clientes na "Pagina de Pedidos / Vendas"
	 *  - Existem 2 GETs nessa classe mas o 1º respondera as requisicoes normais e este as JSON
	 *  
	 * @ResponseBody ==> retorna para TELA DO JS os dados em JSON
	 */
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<Cliente> pesquisar(String nome) {
		validarTamanhoNome(nome);
		return clientes.findByNomeStartingWithIgnoreCase(nome);
	}

	/**
	 * Verifica se o TAMANHO DO NOME na "Pagina de Pedidos" na procura rapida de NOME é VAZIO ou MENOR QUE 3 caracteres
	 * @param nome
	 */
	private void validarTamanhoNome(String nome) {
		if (StringUtils.isEmpty(nome) || nome.length() < 3) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Trata um IllegalArgumentException para esse CONTROLLER
	 *  - Transformando de 500 para 400 (Bad Request == ERRO DO USUARIO e nao do SERVIDOR)
	 */
	@ExceptionHandler(IllegalArgumentException.class) // A classe de excecao que sera tratada
	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build(); // Trata como ERRO de REQUISICAO (erro 400 e nao 500)
	}
	
}
