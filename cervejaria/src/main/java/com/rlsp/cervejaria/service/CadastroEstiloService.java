package com.rlsp.cervejaria.service;



import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.Estilo;
import com.rlsp.cervejaria.repository.EstilosRepository;
import com.rlsp.cervejaria.service.exception.ImpossivelExcluirEntidadeException;
import com.rlsp.cervejaria.service.exception.NomeEstiloJaCadastradoException;



@Service
public class CadastroEstiloService {


	
	@Autowired
	private EstilosRepository estilos;
	
	
//	public CadastroEstiloService(@Qualifier("estilos") EstilosRepository estiloRepository) {
//	    this.estiloRepository = estiloRepository;
//	}
	   
		
	@Transactional
	public Estilo salvar(Estilo estilo) {
		Optional<Estilo> estiloOptional = estilos.findByNomeIgnoreCase(estilo.getNome());
		if (estiloOptional.isPresent()) {
			throw new NomeEstiloJaCadastradoException("Nome do estilo já cadastrado");
		}

		return estilos.saveAndFlush(estilo); //SALVA, COMMITA e RETORNA a informacao inserida
	}
	
	@Transactional
	public void excluir(Estilo estilo) {
		try {
			this.estilos.delete(estilo);
			this.estilos.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar estilo. Já está atrelado a alguma cerveja.");
		}
	}
}
