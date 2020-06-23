package com.rlsp.cervejaria.service.event.venda;

import com.rlsp.cervejaria.model.Venda;

public class VendaEvent  {

	private Venda venda;

	public VendaEvent(Venda venda) {
		this.venda = venda;
	}

	public Venda getVenda() {
		return venda;
	}
}
