package com.rlsp.cervejaria.service;

import com.rlsp.cervejaria.repository.UsuariosRepository;

public enum StatusUsuario {

	ATIVAR {
		@Override
		public void executar(Long[] codigos, UsuariosRepository usuarios) {
			
			usuarios.findByCodigoIn(codigos)
				.forEach(u -> u.setAtivo(true));
		}
	},
	
	DESATIVAR {
		@Override
		public void executar(Long[] codigos, UsuariosRepository usuarios) {
			
			usuarios.findByCodigoIn(codigos)
				.forEach(u -> u.setAtivo(false));
		}
	};
	
	public abstract void executar(Long[] codigos, UsuariosRepository usuarios);
	
}
