package com.rlsp.cervejaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rlsp.cervejaria.model.Cerveja;

@Repository
public interface CervejasRepository extends JpaRepository<Cerveja, Long> {

	 
}