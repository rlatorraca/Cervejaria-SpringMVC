package com.rlsp.cervejaria.repository.helper.venda;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rlsp.cervejaria.dto.VendaMes;
import com.rlsp.cervejaria.dto.VendaOrigem;
import com.rlsp.cervejaria.model.Venda;
import com.rlsp.cervejaria.repository.filter.VendaFilter;

public interface VendasRepositoryQueries {

	Page<Venda> filtrar(VendaFilter filtro, Pageable pageable);
	
	public Venda buscarComItens(Long codigo);
	
	public BigDecimal valorTotalNoAno();
	
	public BigDecimal valorTotalNoMes();
	
	public BigDecimal valorTicketMedioNoAno();
	
	public List<VendaMes> totalPorMes();
	public List<VendaOrigem> totalPorOrigem();
	
}
