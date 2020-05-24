package com.rlsp.cervejaria.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.Cliente;
import com.rlsp.cervejaria.repository.ClientesRepository;
import com.rlsp.cervejaria.service.exception.CpfCnpjClienteJaCadastradoException;



@Service
public class CadastroClienteService {

	@Autowired
	private ClientesRepository clientes;
	
		
	/**
	 * @Transactional = inicia um "EntityManager" para iniciar as transacoes  (como configurado no @EnableTransactionManagement)
	 */
	@Transactional
	public void salvar(Cliente cliente) {
		
		Optional<Cliente> clienteExistente = clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		if (clienteExistente.isPresent()) {
			throw new CpfCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado");
		}
		
		clientes.save(cliente);		
		
		
	}
	
}