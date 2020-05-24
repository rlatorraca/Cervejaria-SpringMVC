package com.rlsp.cervejaria.model.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import com.rlsp.cervejaria.model.Cliente;


/**
 * Faz um Validacao em SEQUENCIA (Comeacndo nos campos de CLIENTES que nao pertencem ao GRUPO (CF, CNPJ))
 * @author rlatorraca
 *
 */
public class ClienteGrupoProvider implements DefaultGroupSequenceProvider<Cliente> {


	@Override
	public List<Class<?>> getValidationGroups(Cliente cliente) {
		
		List<Class<?>> grupos = new ArrayList<>(); //Grupos que serao validados
		
		grupos.add(Cliente.class);  //Adiciona os campos de Cliente.class que nao fazem parte de ALGUM GRUPO
		
		/**
		 * Ira buscar qual o tipo de PESSOA esta sendo selecionado (JURIDICA ou FISICA), assim sera selecionado o Tipo de valicadacao que sera usado
		 */
		if(isPessoaSelecionada(cliente)) {
			grupos.add(cliente.getTipoPessoa().getGrupo());
		}
		
		return grupos;
	}

	private boolean isPessoaSelecionada(Cliente cliente) {
		return cliente != null && cliente.getTipoPessoa() != null;
	}

}
