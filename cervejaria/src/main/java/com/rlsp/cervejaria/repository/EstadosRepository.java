package com.rlsp.cervejaria.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rlsp.cervejaria.model.Estado;

@Qualifier("estados")
@Repository
public interface EstadosRepository extends JpaRepository<Estado, Long> {

	
}	

