package com.rlsp.cervejaria.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rlsp.cervejaria.controller.page.PageWrapper;
import com.rlsp.cervejaria.controller.validator.VendaValidator;
import com.rlsp.cervejaria.mail.Mailer;
import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.StatusVenda;
import com.rlsp.cervejaria.model.TipoPessoa;
import com.rlsp.cervejaria.model.Venda;
import com.rlsp.cervejaria.repository.CervejasRepository;
import com.rlsp.cervejaria.repository.VendasRepository;
import com.rlsp.cervejaria.repository.filter.VendaFilter;
import com.rlsp.cervejaria.security.UsuarioSistema;
import com.rlsp.cervejaria.service.CadastroVendaService;
import com.rlsp.cervejaria.session.TabelasItensSession;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private CervejasRepository cervejas;
	
	@Autowired
	private TabelasItensSession tabelaItensSession;
	
	@Autowired
	private CadastroVendaService cadastroVendaService;
	
	@Autowired
	private VendaValidator vendaValidator;
	
	@Autowired
	private VendasRepository vendas;
	
	@Autowired
	private Mailer mailer; // Usado para o envio de EMAILs
	
	
	
	/**
	 * Adiciona um VALIDADOR para "model/Venda" usando o "validator/VendaValidator"
	 * - Encontrado alguma varivel (metodo) @Valid fara o uso dessa Validacao
	 */
	@InitBinder("venda")
	public void inicializarValidador(WebDataBinder binder) {
		binder.setValidator(vendaValidator);
	}
	
	
//	@GetMapping("/nova")
//	public ModelAndView nova() {
//		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
//		mv.addObject("uuid", UUID.randomUUID().toString());
//		return mv;
//	}
	
	@GetMapping("/nova")
	public ModelAndView nova(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		
		if (StringUtils.isEmpty(venda.getUuid())) {
			venda.setUuid(UUID.randomUUID().toString());
		}
		
		mv.addObject("itens", venda.getItens());
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		mv.addObject("valorTotalItens", tabelaItensSession.getValorTotal(venda.getUuid()));
		
		return mv;
	}
	
	/**
	 * @AuthenticationPrincipal ==> pega o Usuario legado
	 *  - UsuarioSistema EXTEND User
	 */
	@PostMapping(value = "/nova", params = "salvar")
	public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, result);

		if (result.hasErrors()) {
			return nova(venda);
		}
		
		System.out.println(">>> VendasController ==> salvar <<<");
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Venda salva com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping(value = "/nova", params = "emitir")
	public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
				
		System.out.println(">>> VendasController ==> emitir <<<");
		
		venda.setUsuario(usuarioSistema.getUsuario());
						
		cadastroVendaService.emitir(venda);
		attributes.addFlashAttribute("mensagem", "Venda emitida com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping(value = "/nova", params = "enviarEmail")
	public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		
		System.out.println(">>> VendasController ==> enviarEmail <<<");
		
		mailer.enviar(venda); // Envia o Email
		
		
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", String.format("Venda nº [%d] salva com sucesso e e-mail enviado para : %s", venda.getCodigo(), venda.getCliente().getEmail()));
		return new ModelAndView("redirect:/vendas/nova");
	}

	@PostMapping("/item")
	public ModelAndView adicionarItem(Long codigoCerveja , String uuid) {
		Cerveja cerveja = cervejas.findById(codigoCerveja).orElse(null);
		tabelaItensSession.adicionarItem(uuid, cerveja, 1);
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItensSession.getItens(uuid));
		mv.addObject("valorTotal", tabelaItensSession.getValorTotal(uuid));
		return mv;
	}
	
	@PutMapping("/item/{codigoCerveja}")
	public ModelAndView alterarQuantidadeItem(@PathVariable Long codigoCerveja, Integer quantidade , String uuid) {
		Cerveja cerveja = cervejas.findById(codigoCerveja).orElse(null);
		tabelaItensSession.alterarQuantidadeItens(uuid, cerveja, quantidade);
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItensSession.getItens(uuid));
		mv.addObject("valorTotal", tabelaItensSession.getValorTotal(uuid));
		return mv;
	}
	
	/**
	 * @PathVariable("codigoCerveja") Cerveja cerveja
	 *  - Como estamos integrando o SPRING MVC  + SPRING JPA, pode-se usar esse codigo mostrando ao SPRING MVC que nao precisamos
	 *    usar a funcao "findById()" para pegar uma Cerveja.
	 *    Entao, iremos usar no config/WebConfig (o metodo "domainClassConverter()") para mostrar que ela pode buscar a cerveja direto
	 *    usando pelo codigoCerveja passado no mapeamento do metdodo "/item/{codigoCerveja}"
	 *  - Fara o uso AUTOMATICO do "findById()" para localizar a Cerveja que se quer.
	 */
	@DeleteMapping("/item/{uuid}/{codigoCerveja}")
	public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja, @PathVariable String uuid) {
		tabelaItensSession.excluirItem(uuid, cerveja);
		return mvTabelaItensVenda(uuid);
	}
	
	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter,
			@PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView mv = new ModelAndView("/venda/PesquisaVendas");
		
		mv.addObject("todosStatus", StatusVenda.values());
		mv.addObject("tiposPessoa", TipoPessoa.values());
		
		PageWrapper<Venda> paginaWrapper = new PageWrapper<>(vendas.filtrar(vendaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	private ModelAndView mvTabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItensSession.getItens(uuid));
		mv.addObject("valorTotal", tabelaItensSession.getValorTotal(uuid));
		return mv;
	}
	
	private void validarVenda(Venda venda, BindingResult result) {
		
		//Adiciona os itens dentro da Venda, ANTES de fazer a validacao dos Itens
		venda.adicionarItens(tabelaItensSession.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		vendaValidator.validate(venda, result); // faz a VALIDACAO usando "VendaValidator"
	}

	
}
