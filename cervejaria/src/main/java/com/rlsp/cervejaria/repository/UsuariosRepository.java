package com.rlsp.cervejaria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rlsp.cervejaria.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByEmail(String email);

}
