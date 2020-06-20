package com.rlsp.cervejaria.service.event.cerveja;

import org.springframework.util.StringUtils;

import com.rlsp.cervejaria.model.Cerveja;

public class CervejaSalvaEvent {

	private Cerveja cerveja;

	public CervejaSalvaEvent(Cerveja cerveja) {
		this.cerveja = cerveja;
	}

	public Cerveja getCerveja() {
		return cerveja;
	}
	
	/**
	 * Verifica se tem um FOTO vinculada ao nome da cerveja	
	 */
	public boolean temFoto() {
		return !StringUtils.isEmpty(cerveja.getFoto());
	}
	
	/**
	 * Verifica se a FOTO é NOVA	 * 
	 */
	public boolean novaFoto() {
		return cerveja.isNovaFoto();
	}
}
