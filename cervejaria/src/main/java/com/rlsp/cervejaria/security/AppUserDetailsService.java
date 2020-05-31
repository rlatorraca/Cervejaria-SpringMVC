package com.rlsp.cervejaria.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rlsp.cervejaria.model.Usuario;
import com.rlsp.cervejaria.repository.UsuariosRepository;
import com.rlsp.cervejaria.repository.helper.usuario.UsuariosRepositoryQueries;

/**
 * @Service ==> para se encontrada pela classe "config/SecurityConfig"
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuariosRepository usuarios;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		//Procura o usuario ativo
		Optional<Usuario> usuarioOptional = usuarios.porEmailEAtivo(email);
		
		Usuario usuario = usuarioOptional.orElseThrow(
						() -> new UsernameNotFoundException("Usuário e/ou senha incorretos")
					);
		
		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}

	
	/**
	 * Para pegar PERMISSOES
	 * 
	 */
	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		
		// Lista de Permissoes do Usuario
		List<String> permissoes = usuarios.permissoes(usuario);
		permissoes.forEach(
				p -> authorities.add(new SimpleGrantedAuthority(p.toUpperCase()))
		);
		
		return authorities;
	}

}
	