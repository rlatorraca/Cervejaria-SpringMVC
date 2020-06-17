package com.rlsp.cervejaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rlsp.cervejaria.model.Venda;
import com.rlsp.cervejaria.repository.helper.venda.VendasRepositoryQueries;


public interface VendasRepository extends JpaRepository<Venda, Long>, VendasRepositoryQueries {

}
