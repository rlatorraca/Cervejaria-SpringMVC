package com.rlsp.cervejaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rlsp.cervejaria.model.Venda;


public interface VendasRepository extends JpaRepository<Venda, Long> {

}
