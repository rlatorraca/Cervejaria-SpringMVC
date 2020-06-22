package com.rlsp.cervejaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rlsp.cervejaria.model.Cidade;
import com.rlsp.cervejaria.model.Estado;
import com.rlsp.cervejaria.repository.helper.cidade.CidadesRepositoryQueries;

@Repository
public interface CidadesRepository  extends JpaRepository<Cidade, Long>, CidadesRepositoryQueries{

	public List<Cidade> findByEstadoCodigo(Long codigoEstado);
		
	public Optional<Cidade> findByNomeAndEstado(String nome, Estado estado);
	
	@Query("select c from Cidade c join fetch c.estado where c.codigo = :codigo")
	public Cidade findByCodigoFetchingEstado(@Param("codigo") Long codigo);
	
}
