package com.rlsp.cervejaria.venda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.ItemVenda;



public class TabelaItensVenda {

	private List<ItemVenda> itens = new ArrayList<>();
	
	
	//Calcula o VALOR TOTAL com os ITENS DE VENDA
	public BigDecimal getValorTotal() {
		return itens.stream()
				.map(ItemVenda::getValorTotal) // Chama o metodo getValorTotal()
				.reduce(BigDecimal::add)       //BigDecimal::add ==> faz a SOMA
				.orElse(BigDecimal.ZERO);	  // Se nao tiver VALOR, retorna ZERO
	}
	
	
	public void adicionarItem(Cerveja cerveja, Integer quantidade) {
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setCerveja(cerveja);
		itemVenda.setQuantidade(quantidade);
		itemVenda.setValorUnitario(cerveja.getValor());
		
		itens.add(itemVenda);
	}
}
