package com.rlsp.cervejaria.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.repository.CervejasRepository;
import com.rlsp.cervejaria.service.event.cerveja.CervejaSalvaEvent;
import com.rlsp.cervejaria.service.exception.ImpossivelExcluirEntidadeException;
import com.rlsp.cervejaria.storage.FotoStorage;



@Service
public class CadastroCervejaService {

	@Autowired
	private CervejasRepository cervejas;
	
	@Autowired
	private FotoStorage fotoStorage;
	
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

	@Transactional
	public void excluir(Cerveja cerveja) {
		try {
			String foto = cerveja.getFoto(); // Pega o nome da FOTO da cerveja a ser excluida, para entao, apagar a foto
			cervejas.delete(cerveja); // Pede para DELETAR a cerveja
			cervejas.flush();	      // Pede para fazer a TENTATIVA em ato continuo de delecao
			fotoStorage.excluir(foto);
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar cerveja. Já foi usada em alguma venda.");
		}
	}
	
}