package com.rlsp.cervejaria.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Strings;
import com.rlsp.cervejaria.model.Usuario;
import com.rlsp.cervejaria.repository.UsuariosRepository;
import com.rlsp.cervejaria.service.exception.EmailUsuarioJaCadastradoException;
import com.rlsp.cervejaria.service.exception.SenhaObrigatoriaUsuarioException;



@Service
public class CadastroUsuarioService {

	@Autowired
	private UsuariosRepository usuarios;
		
	
	@Autowired	
	private PasswordEncoder bcrypt;

	
	
	@Transactional
	public void salvar(Usuario usuario) {
		Optional<Usuario> usuarioExistente = usuarios.findByEmail(usuario.getEmail());
		/*
		 * !usuarioExistente.get().equals(usuario) ==> usado para EDITAR
		 */
		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new EmailUsuarioJaCadastradoException("E-mail já cadastrado");
		}
		
		if (usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha())) {
			throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
		}
		
		if (usuario.isNovo()) {
			usuario.setSenha(this.bcrypt.encode(usuario.getSenha()));
			usuario.setConfirmacaoSenha(usuario.getSenha());
		}
		if(usuario.isEdicao() && StringUtils.isEmpty(usuario.getSenha())){
		
			usuario.setSenha(usuarioExistente.get().getSenha()); // Copia a senha ja inserida pelo USUARIO no caso de EDICAO do Usuario
			usuario.setConfirmacaoSenha(usuario.getSenha());
			
			if(usuario.getAtivo() == null) {
				usuario.setAtivo(usuarioExistente.get().getAtivo());
			}
			
		}
		
		usuarios.save(usuario);
	}
	
	@Transactional
	public void alterarStatus(Long[] codigos, StatusUsuario statusUsuario) {
		statusUsuario.executar(codigos, usuarios);
	}
}