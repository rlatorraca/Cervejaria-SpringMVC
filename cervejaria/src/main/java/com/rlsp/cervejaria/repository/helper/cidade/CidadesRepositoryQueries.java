package com.rlsp.cervejaria.repository.helper.cidade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rlsp.cervejaria.model.Cidade;
import com.rlsp.cervejaria.repository.filter.CidadeFilter;

public interface CidadesRepositoryQueries {

	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable);
	
}
