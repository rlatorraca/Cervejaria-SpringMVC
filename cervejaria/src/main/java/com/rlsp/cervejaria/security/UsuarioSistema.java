package com.rlsp.cervejaria.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.rlsp.cervejaria.model.Usuario;
/**
 * Classe Usada para mostar o NOME / EMAIL do usuario logado na pagina
 *  - Usa Heranca para herder da class "User"(org.springframework.security.core.userdetails.User)
 * 
 */

public class UsuarioSistema extends User{

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities); // Chama o construtor do PAI (User do Spring)
		this.usuario = usuario;
	}

	//Recupera as informacoes do USUARIO
	public Usuario getUsuario() {
		return usuario;
	}
}
