package com.rlsp.cervejaria.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import com.rlsp.cervejaria.model.Usuario;

public interface UsuariosRepositoryQueries {

	public Optional<Usuario> porEmailEAtivo(String email);
	
	public List<String> permissoes(Usuario usuario);
	
}
