package com.rlsp.cervejaria.service.event.venda;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.ItemVenda;
import com.rlsp.cervejaria.repository.CervejasRepository;

@Component // Usando para fazer Atualizcao dos ESTOQUE após uma venda
public class VendaListener {

	@Autowired
	private CervejasRepository cervejas;
	
	@EventListener
	public void vendaEmitida(VendaEvent vendaEvent) {
		// Faz a varredura nos ITENS (cada cerveja) de um VENDA
		for (ItemVenda item : vendaEvent.getVenda().getItens()) {
			Cerveja cerveja = cervejas.findById(item.getCerveja().getCodigo()).orElse(null);
			cerveja.setQuantidadeEstoque(cerveja.getQuantidadeEstoque() - item.getQuantidade()); // Faz o calcula da atualizcao
			cervejas.save(cerveja);
		}
	}
	
}
