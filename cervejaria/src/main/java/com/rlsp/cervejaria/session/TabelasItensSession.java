package com.rlsp.cervejaria.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.ItemVenda;

/**
 * Esta sera a SESSAO de CADA USUARIO
 */

@SessionScope // Cria um OBJETO (usuario) por SESSAO
@Component
public class TabelasItensSession {

	/**
	 * Usa-se SET pois NAO existirao SESSOES REPITIDAS
	 *  - Identifica por "TabelaItensVenda.uuid"
	 *    - UUID é unico
	 */
	private Set<TabelaItensVenda> tabelas = new HashSet<>();

	public void adicionarItem(String uuid, Cerveja cerveja, int quantidade) {
		TabelaItensVenda tabela = buscarTabelaPorUuid(uuid);
		tabela.adicionarItem(cerveja, quantidade);
		tabelas.add(tabela);
	}

	public void alterarQuantidadeItens(String uuid, Cerveja cerveja, Integer quantidade) {
		TabelaItensVenda tabela = buscarTabelaPorUuid(uuid);
		tabela.alterarQuantidadeDeCervejas(cerveja, quantidade);
	}

	public void excluirItem(String uuid, Cerveja cerveja) {
		TabelaItensVenda tabela = buscarTabelaPorUuid(uuid);
		tabela.excluirCervejaTela(cerveja);
	}

	public List<ItemVenda> getItens(String uuid) {
		return buscarTabelaPorUuid(uuid).getItens();
	}
	
	public Object getValorTotal(String uuid) {		
		return buscarTabelaPorUuid(uuid).getValorTotal();
	}
	
	private TabelaItensVenda buscarTabelaPorUuid(String uuid) {
		TabelaItensVenda tabela = tabelas.stream()
				.filter(t -> t.getUuid().equals(uuid)) // Verifica se a TABELA de CERVEJAS tem o mesmo UUID e existe
				.findAny()
				.orElse(new TabelaItensVenda(uuid)); // Se NAO EXISTIR sera criada uma TABELA com o UUID
		return tabela;
	}

	
}
