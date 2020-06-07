package com.rlsp.cervejaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rlsp.cervejaria.model.Cliente;
import com.rlsp.cervejaria.repository.filter.ClienteFilter;
import com.rlsp.cervejaria.repository.helper.cliente.ClientesRepositoryQueries;

@Qualifier("clientes")
@Repository
public interface ClientesRepository extends JpaRepository<Cliente, Long>, ClientesRepositoryQueries{

	public Optional<Cliente> findByCpfOuCnpj(String cpfOuCnpjString);

	public List<Cliente> findByNomeStartingWithIgnoreCase(String nome);

	
}