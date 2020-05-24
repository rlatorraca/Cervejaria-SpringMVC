package com.rlsp.cervejaria.repository.helper.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rlsp.cervejaria.model.Cliente;
import com.rlsp.cervejaria.repository.filter.ClienteFilter;

public interface ClientesRepositoryQueries {

	//public List<Cerveja> filtrar(CervejaFilter filtro);
	
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable);
	
}
