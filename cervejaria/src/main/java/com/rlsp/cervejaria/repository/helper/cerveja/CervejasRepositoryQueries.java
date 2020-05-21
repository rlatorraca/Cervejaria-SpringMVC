package com.rlsp.cervejaria.repository.helper.cerveja;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.repository.filter.CervejaFilter;

public interface CervejasRepositoryQueries {

	//public List<Cerveja> filtrar(CervejaFilter filtro);
	
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable);
	
}
