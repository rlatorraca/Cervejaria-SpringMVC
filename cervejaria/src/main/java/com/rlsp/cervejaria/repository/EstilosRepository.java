package com.rlsp.cervejaria.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rlsp.cervejaria.model.Estilo;
import com.rlsp.cervejaria.repository.helper.estilo.EstilosRepositoryQueries;



//@Qualifier("estilos")
@Repository
public interface EstilosRepository extends JpaRepository<Estilo, Long>,  EstilosRepositoryQueries{

	public Optional<Estilo> findByNomeIgnoreCase(String nome);
	
	public Estilo findByCodigo(Long codigo);
	
}