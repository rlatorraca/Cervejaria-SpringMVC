package com.rlsp.cervejaria.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.Cidade;
import com.rlsp.cervejaria.model.Cliente;
import com.rlsp.cervejaria.repository.CidadesRepository;
import com.rlsp.cervejaria.repository.ClientesRepository;
import com.rlsp.cervejaria.service.exception.CpfCnpjClienteJaCadastradoException;
import com.rlsp.cervejaria.service.exception.ImpossivelExcluirEntidadeException;



@Service
public class CadastroClienteService {

	@Autowired
	private ClientesRepository clientes;
	
	@Autowired 
	private CidadesRepository cidades;
	
		
	/**
	 * @Transactional = inicia um "EntityManager" para iniciar as transacoes  (como configurado no @EnableTransactionManagement)
	 */
	@Transactional
	public void salvar(Cliente cliente) {
		
		if (cliente.isNovo()) {
			this.clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao())
						 .ifPresent(c -> {
							 throw new CpfCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado"); 
						 });
		}
		
		/*
		Optional<Cliente> clienteExistente = clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		if (clienteExistente.isPresent()) {
			throw new CpfCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado");
		}
		*/
		
		clientes.save(cliente);				
		
	}
	
	@Transactional(readOnly = true)
	public void comporDadosEndereco(Cliente cliente) {
		if (cliente.getEndereco() == null 
				|| cliente.getEndereco().getCidade() == null
				|| cliente.getEndereco().getCidade().getCodigo() == null) {
			return;
		}

		Cidade cidade = this.cidades.findByCodigoFetchingEstado(cliente.getEndereco().getCidade().getCodigo());

		cliente.getEndereco().setCidade(cidade);
		cliente.getEndereco().setEstado(cidade.getEstado());
	}

	@Transactional
	public void excluir(Cliente cliente) {
		try {
			this.clientes.delete(cliente);
			this.clientes.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar cliente. Já está atrelado a alguma venda.");
		}
	}

	
}