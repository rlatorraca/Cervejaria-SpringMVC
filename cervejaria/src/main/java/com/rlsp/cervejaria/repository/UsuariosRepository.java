package com.rlsp.cervejaria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rlsp.cervejaria.model.Usuario;
import com.rlsp.cervejaria.repository.helper.usuario.UsuariosRepositoryQueries;

public interface UsuariosRepository extends JpaRepository<Usuario, Long>, UsuariosRepositoryQueries {

	public Optional<Usuario> findByEmail(String email);

}
