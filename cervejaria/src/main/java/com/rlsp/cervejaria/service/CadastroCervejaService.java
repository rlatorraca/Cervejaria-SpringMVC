package com.rlsp.cervejaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.repository.CervejasRepository;
import com.rlsp.cervejaria.service.event.cerveja.CervejaSalvaEvent;



@Service
public class CadastroCervejaService {

	@Autowired
	private CervejasRepository cervejas;
	
	@Autowired
	private ApplicationEventPublisher publisher; // Serve para publicando um EVENTO para a CERVEJA (no casa criar o THUMBNAIL da Cerveja)
	
	/**
	 * @Transactional = inicia um "EntityManager" para iniciar as transacoes  (como configurado no @EnableTransactionManagement)
	 */
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejas.save(cerveja);
		
		/**
		 * Publica uma cerveja
		 */
		publisher.publishEvent(new CervejaSalvaEvent(cerveja));
	}
	
}