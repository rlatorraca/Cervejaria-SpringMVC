package com.rlsp.cervejaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rlsp.cervejaria.model.Usuario;
import com.rlsp.cervejaria.repository.filter.UsuarioFilter;
import com.rlsp.cervejaria.repository.helper.usuario.UsuariosRepositoryQueries;

public interface UsuariosRepository extends JpaRepository<Usuario, Long>, UsuariosRepositoryQueries {

	public Optional<Usuario> findByEmail(String email);

	public List<Usuario> findByCodigoIn(Long[] codigos);

	public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable);
}
