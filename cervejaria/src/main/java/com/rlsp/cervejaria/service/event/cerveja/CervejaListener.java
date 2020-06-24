package com.rlsp.cervejaria.service.event.cerveja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.rlsp.cervejaria.storage.FotoStorage;

@Component // Sera um Compenete do SPRING para poder injetar o "FotoStorage"
public class CervejaListener {

	@Autowired
	private FotoStorage fotoStorage;
	
	/**
	 * Chama Toda vez que  UMA CERVEJA é salva 
	 */
	@EventListener(condition = "#evento.temFoto() and #evento.novaFoto()") // so vai chamar se TIVER UMA FOTO vinculada
	public void cervejaSalva(CervejaSalvaEvent evento) {
		//fotoStorage.salvar(evento.getCerveja().getFoto());
	}
	
}