package com.rlsp.cervejaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.repository.CervejasRepository;



@Service
public class CadastroCervejaService {

	@Autowired
	private CervejasRepository cervejas;
	
	/**
	 * @Transactional = inicia um "EntityManager" para iniciar as transacoes  (como configurado no @EnableTransactionManagement)
	 */
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejas.save(cerveja);
	}
	
}