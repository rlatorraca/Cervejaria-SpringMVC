package com.rlsp.cervejaria.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.Cidade;
import com.rlsp.cervejaria.repository.CidadesRepository;
import com.rlsp.cervejaria.service.exception.ImpossivelExcluirEntidadeException;
import com.rlsp.cervejaria.service.exception.NomeCidadeJaCadastradaException;

@Service
public class CadastroCidadeService {

	@Autowired
	private CidadesRepository cidades;
	
	@Transactional
	public void salvar(Cidade cidade) {
		if (cidade.getEstado() != null) {
			Optional<Cidade> cidadeExistente = cidades.findByNomeAndEstado(cidade.getNome(), cidade.getEstado());
			if (cidadeExistente.isPresent()) {
				throw new NomeCidadeJaCadastradaException("Nome de cidade já cadastrado");
			}
			
			cidades.save(cidade);
		} 
	}

	@Transactional
	public void excluir(Cidade cidade) {
		try {
			this.cidades.delete(cidade);
			this.cidades.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar cidade. O registro está sendo usado.");
		}
	}
	
}
