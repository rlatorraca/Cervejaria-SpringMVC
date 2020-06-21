package com.rlsp.cervejaria.repository.helper.venda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rlsp.cervejaria.model.Venda;
import com.rlsp.cervejaria.repository.filter.VendaFilter;

public interface VendasRepositoryQueries {

	Page<Venda> filtrar(VendaFilter filtro, Pageable pageable);
	
	public Venda buscarComItens(Long codigo);
	
}
