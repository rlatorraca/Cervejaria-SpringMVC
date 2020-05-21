package com.rlsp.cervejaria.repository.helper.estilo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rlsp.cervejaria.model.Estilo;
import com.rlsp.cervejaria.repository.filter.EstiloFilter;

public interface EstilosRepositoryQueries {

	public Page<Estilo> filtrar(EstiloFilter filtro, Pageable pageable);
}
