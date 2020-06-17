package com.rlsp.cervejaria.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.StatusVenda;
import com.rlsp.cervejaria.model.Venda;
import com.rlsp.cervejaria.repository.VendasRepository;



@Service
public class CadastroVendaService {

	@Autowired
	private VendasRepository vendas;
	
	@Transactional
	public void salvar(Venda venda) {
		/**
		 * Pega a DATA / HORA para nova VENDA
		 */
		if (venda.isNova()) {
			venda.setDataCriacao(LocalDateTime.now());
		}
		
				
		/**
		 * Cria a DATA_HORA_ENTREGA
		 *  - Se EXSITIR uma DATA de ENTREGA
		 */
		if (venda.getDataEntrega() != null) {
			venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(), venda.getHorarioEntrega() != null ? venda.getHorarioEntrega() : LocalTime.MIDNIGHT));
		}
		
		vendas.save(venda);
	}
	
	
	@Transactional
	public void emitir(Venda venda) {
		venda.setStatus(StatusVenda.EMITIDA);
		salvar(venda);
	}

	
	
}
