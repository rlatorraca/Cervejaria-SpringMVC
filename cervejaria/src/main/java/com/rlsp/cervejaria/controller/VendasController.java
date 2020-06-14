package com.rlsp.cervejaria.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.repository.CervejasRepository;
import com.rlsp.cervejaria.session.TabelaItensVenda;
import com.rlsp.cervejaria.session.TabelasItensSession;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private CervejasRepository cervejas;
	
	@Autowired
	private TabelasItensSession tabelaItensSession;
	
	//@Autowired
	//private CadastroVendaService cadastroVendaService;
	
	//@Autowired
	///private VendaValidator vendaValidator;
	
	//@Autowired
	//private Vendas vendas;
	
	
	@GetMapping("/nova")
	public ModelAndView nova() {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		mv.addObject("uuid", UUID.randomUUID().toString());
		return mv;
	}
	
//	@GetMapping("/nova")
//	public ModelAndView nova(Venda venda) {
//		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
//		
//		if (StringUtils.isEmpty(venda.getUuid())) {
//			venda.setUuid(UUID.randomUUID().toString());
//		}
//		
//		mv.addObject("itens", venda.getItens());
//		mv.addObject("valorFrete", venda.getValorFrete());
//		mv.addObject("valorDesconto", venda.getValorDesconto());
//		mv.addObject("valorTotalItens", tabelaItensSession.getValorTotal(venda.getUuid()));
//		
//		return mv;
//	}

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

	private ModelAndView mvTabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItensSession.getItens(uuid));
		mv.addObject("valorTotal", tabelaItensSession.getValorTotal(uuid));
		return mv;
	}

	
}
