package com.rlsp.cervejaria.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Usado na ORDENACAO das PAGINAS e Formacao das URIs * 
 */
public class PageWrapper<T> {

	private Page<T> page;
	private UriComponentsBuilder uriBuilder;   // Contrutor de URI

	/**
	 * HttpServletRequest ==> usado para criar o uriBuilder ,pois CONTEM e TRAZ informacoes da PESQUISA / FILTRO
	 */
	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		
		String httpUrl = httpServletRequest.getRequestURL()
				.append(httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
				.toString()
					.replaceAll("\\+", "%20")
					.replaceAll("excluido", ""); // Usado no JS de Delecao das cervejas
		this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl); // montar URI's
		//this.uriBuilder = ServletUriComponentsBuilder.fromRequest(httpServletRequest);
	}
	
	
	/**
	 * METODOS que se pretende DISPONIBILIZAR de PAGE
	 * 
	 */
	public List<T> getConteudo() {
		return page.getContent(); // Pega o CONTEUDO com as cervejasf iltradas
	}
	
	public boolean isVazia() {
		return page.getContent().isEmpty();   // Testa se a pagina esta vazia (sem cervejas achadas /filtradas)
	}
	
	public int getAtual() {
		return page.getNumber();	//Pega a pagina que esta senod VISUALIDZADA
	}
	
	public boolean isPrimeira() {
		return page.isFirst();		// Pega a 1ª pagina da pesquisa / filtro
	}
	
	public boolean isUltima() {
		return page.isLast();	 	// Pega aULTIMA pagina da pesquisa / filtro
	}
	
	public int getTotal() {
		return page.getTotalPages(); //Pega a quantidade TOTAL de paginas
	}
	
	/**
	 * Cria uma URL para pagina a ser VISUALIZADA 
	 *  - "replaceQueryParam" : substitui (se ja tiver na pagina) ou adiciona o parametro "page" na pagina 
	 *  - "build(true).encode()" : serve para codificarf e decoficar valores decimais (valor unitario, no caso)
	 */
	public String urlParaPagina(int pagina) {
		return uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
	}
	
	
	/**
	 * Usado para ORDENACAO das paginas	 * 
	 */
	public String urlOrdenada(String propriedade) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder.fromUriString(uriBuilder.build(true).encode().toUriString()); // Criando a URL
		
		/**
		 * propriedade : é o compo para ORDENADCAO
		 */
		String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade));
		
		return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
	}
	
	/**
	 * Usado para ALTERNAR entre os tipos de ordenacao DESC e ASC na pagina de pesquisa de cerveja
	 * 
	 */
	public String inverterDirecao(String propriedade) {
		String direcao = "asc";
		
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}
		
		return direcao;
	}
	
	/**
	 * Verifica se esta em DESC, if TRUE , DIRECAO =  DESCENDENTE
	 */
	public boolean descendente(String propriedade) {
		return inverterDirecao(propriedade).equals("asc");
	}
	
	/**
	 * Checa se JA ESTA ORDENADA por alguma campo ou NAO
	 *  - NULL = FALSE
	 *  - TEM ALGUMA ORDENACAO = TRUE
	 */
	public boolean ordenada(String propriedade) {
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null; 
		
		if (order == null) {
			return false;
		}
		
		return page.getSort().getOrderFor(propriedade) != null ? true : false; // 
	}
	
}
