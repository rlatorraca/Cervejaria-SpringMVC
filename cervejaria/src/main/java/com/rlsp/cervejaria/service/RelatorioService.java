package com.rlsp.cervejaria.service;

import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rlsp.cervejaria.dto.PeriodoRelatorio;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService {

	@Autowired
	private DataSource dataSource;
	
	public byte[] gerarRelatorioVendasEmitidas(PeriodoRelatorio periodoRelatorio) throws Exception {
		// periodoRelatorio.getDataInicio() ==> pega a data inserida na tela
		Date dataInicio = Date.from(LocalDateTime.of(periodoRelatorio.getDataInicio(), LocalTime.of(0, 0, 0))
				.atZone(ZoneId.systemDefault()).toInstant());
		// periodoRelatorio.getDataFim() ==> pega a data inserida na tela
		Date dataFim = Date.from(LocalDateTime.of(periodoRelatorio.getDataFim(), LocalTime.of(23, 59, 59))
				.atZone(ZoneId.systemDefault()).toInstant());
		
		Map<String, Object> parametros = new HashMap<>(); // Cria os parametros para o relatorio
		parametros.put("format", "pdf");
		parametros.put("data_inicio", dataInicio);
		parametros.put("data_fim", dataFim);
		
		InputStream inputStream = this.getClass()
				.getResourceAsStream("/reports/relatorio_vendas_emitidas.jasper"); // Pega o relatorio da fonte
		
		Connection con = this.dataSource.getConnection(); // Conexao com o DB
		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, con); // Entrada + parametros do relatorio + conexao com o DB
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			con.close();
		}
	}
}
