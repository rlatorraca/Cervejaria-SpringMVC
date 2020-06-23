package com.rlsp.cervejaria.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.StatusVenda;
import com.rlsp.cervejaria.model.Venda;
import com.rlsp.cervejaria.repository.VendasRepository;
import com.rlsp.cervejaria.service.event.venda.VendaEvent;



@Service
public class CadastroVendaService {

	@Autowired
	private VendasRepository vendas;
	
	@Autowired
	private ApplicationEventPublisher publisher; // Usado para atualizar o ESTOQUE em caso de uma VENDA
	
	
	@Transactional
	public Venda salvar(Venda venda) {
		
		if (venda.isSalvarProibido()) {
			throw new RuntimeException("Usuário tentando salvar uma venda proibida");
		}
		
		/**
		 * Pega a DATA / HORA para nova VENDA
		 */
		if (venda.isNova()) {
			venda.setDataCriacao(LocalDateTime.now());
		} else {
			Venda vendaExistente = vendas.findById(venda.getCodigo()).orElse(null);
			venda.setDataCriacao(vendaExistente.getDataCriacao());
		}
		
				
		/**
		 * Cria a DATA_HORA_ENTREGA
		 *  - Se EXSITIR uma DATA de ENTREGA
		 */
		if (venda.getDataEntrega() != null) {
			venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(), venda.getHorarioEntrega() != null ? venda.getHorarioEntrega() : LocalTime.MIDNIGHT));
		}
		
		return vendas.saveAndFlush(venda); // saveAndFlush ==> save + reorganiza os valores
	}
	
	
	@Transactional
	public void emitir(Venda venda) {
		venda.setStatus(StatusVenda.EMITIDA);		
		salvar(venda);
		
		publisher.publishEvent(new VendaEvent(venda));
		
	}

	/**
	 * principal.usuario ==> QUEM CRIOU o Pedido
	 * hasRole('CANCELAR_VENDA') ==> USUARIO com a ROLE "CANCELAR_VENDA
	 * @param venda
	 */
	@PreAuthorize("#venda.usuario == principal.usuario or hasRole('CANCELAR_VENDA')")
	@Transactional
	public void cancelar(Venda venda) {
		Venda vendaExistente = vendas.findById(venda.getCodigo()).orElse(null);
		
		vendaExistente.setStatus(StatusVenda.CANCELADA);
		vendas.save(vendaExistente);
	}
	
}
