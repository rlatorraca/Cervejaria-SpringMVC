package com.rlsp.cervejaria.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.ItemVenda;

@SessionScope // Cria um OBJETO (usuario) por SESSAO
@Component
public class TabelaItensVenda {

	private String uuid;
	private List<ItemVenda> itens = new ArrayList<>();
	
	
	//Calcula o VALOR TOTAL com os ITENS DE VENDA
	public BigDecimal getValorTotal() {
		return itens.stream()
				.map(ItemVenda::getValorTotal) // Chama o metodo getValorTotal()
				.reduce(BigDecimal::add)       //BigDecimal::add ==> faz a SOMA
				.orElse(BigDecimal.ZERO);	  // Se nao tiver VALOR, retorna ZERO
	}
	
	
	public void adicionarItem(Cerveja cerveja, Integer quantidade) {
		
		// Pega a CERVEJA incluida em Vendas / Pedidos e cria um STREAM verificando QUAL EH A CERVEJA
		Optional<ItemVenda> itemVendaOptional = itens.stream()
				.filter(i -> i.getCerveja().equals(cerveja))
				.findAny();
		
		// Faz a VERIFICACAO se a CERVEJA ja EXISTE dentro do PEDIDO e apenas  ACRESCENTA a quantidade REQUERIDA
		//	- CASO CONTRARIO
		//		--> CRIA-SE uma NOVA linha / cerveja no pedido 
		ItemVenda itemVenda = null;
		if (itemVendaOptional.isPresent()) {
			itemVenda = itemVendaOptional.get();
			itemVenda.setQuantidade(itemVenda.getQuantidade() + quantidade);
		} else {
			itemVenda = new ItemVenda();
			itemVenda.setCerveja(cerveja);
			itemVenda.setQuantidade(quantidade);
			itemVenda.setValorUnitario(cerveja.getValor());
			itens.add(0, itemVenda); // o "0" faz INCLUSAO na 1ª posicao
		}
	}


	public int total() {
		return itens.size();
	}
	
	public List<ItemVenda> getItens() {
		return itens;
	}
	
	@SuppressWarnings("unused")
	private Optional<ItemVenda> buscarItemPorCerveja(Cerveja cerveja) {
		return itens.stream()
				.filter(i -> i.getCerveja().equals(cerveja))
				.findAny();
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabelaItensVenda other = (TabelaItensVenda) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
}
