package com.rlsp.cervejaria.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.repository.helper.cerveja.CervejasRepositoryQueries;

@Qualifier("cervejas")
@Repository
public interface CervejasRepository extends JpaRepository<Cerveja, Long>, CervejasRepositoryQueries {

	 
}