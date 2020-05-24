package com.rlsp.cervejaria.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
}
