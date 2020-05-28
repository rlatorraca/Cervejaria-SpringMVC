package com.rlsp.cervejaria.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.Usuario;
import com.rlsp.cervejaria.repository.UsuariosRepository;
import com.rlsp.cervejaria.service.exception.EmailUsuarioJaCadastradoException;



@Service
public class CadastroUsuarioService {

	@Autowired
	private UsuariosRepository usuarios;
	
	@Transactional
	public void salvar(Usuario usuario) {
		Optional<Usuario> usuarioExistente = usuarios.findByEmail(usuario.getEmail());
		if (usuarioExistente.isPresent()) {
			throw new EmailUsuarioJaCadastradoException("E-mail já cadastrado");
		}
		
		usuarios.save(usuario);
	}
	
}