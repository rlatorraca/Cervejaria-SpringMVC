package com.rlsp.cervejaria;

import java.time.LocalDate;

public class CriarDatasMilisegundos {

	public static void main(String[] args) {
	
		LocalDate date1 = LocalDate.of(2019, 01, 01);
		System.out.println("01/01/2019 : " + date1.toEpochDay()*24*60*60);
		LocalDate date2 = LocalDate.of(2020, 06, 28);
		System.out.println("28/06/2020 : " + date2.toEpochDay()*24*60*60);
		
		/*
		 * insert into venda (data_criacao, valor_total, status, codigo_cliente, codigo_usuario)
				values (from_unixtime(ROUND(RAND()*(1593302400 - 1546300800) + 1546300800))
    			, ROUND(rand()* 50000,2)
    			, 'EMITIDA'
    			, round(rand()*12)+1
    			, round(rand()*8)+1)
		 */

	}

}
