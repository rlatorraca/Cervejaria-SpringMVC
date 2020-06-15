package com.rlsp.cervejaria.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlsp.cervejaria.model.ItemVenda;
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
		 * Calcula o VALOR TOTAL da VENDA
		 */
		BigDecimal valorTotalItens = venda.getItens().stream()
				.map(ItemVenda::getValorTotal)
				.reduce(BigDecimal::add)
				.get();
		
		BigDecimal valorTotal = calcularValorTotal(valorTotalItens, venda.getValorFrete(), venda.getValorDesconto());
		venda.setValorTotal(valorTotal);
		
		/**
		 * Cria a DATA_HORA_ENTREGA
		 *  - Se EXSITIR uma DATA de ENTREGA
		 */
		if (venda.getDataEntrega() != null) {
			venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(), venda.getHorarioEntrega()));
		}
		
		vendas.save(venda);
	}

	private BigDecimal calcularValorTotal(BigDecimal valorTotalItens, BigDecimal valorFrete, BigDecimal valorDesconto) {
		BigDecimal valorTotal = valorTotalItens
				.add(Optional.ofNullable(valorFrete).orElse(BigDecimal.ZERO)) // Cria um OPTIONAL que pode ser NULO (e ser = ZERO se nulo)
				.subtract(Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO));
		return valorTotal;
	}
	
}
