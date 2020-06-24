package com.rlsp.cervejaria.config.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * Usado para fazer a conversacao de VALORES NUMERICOS em caso de INTERNACIONALIZACAO 
 *	- Mantendo os valores em R$ mesmo se a lingua for em INGLES oura qualquer que tenha moeda distinta
 * @author rlatorraca
 */

public class BigDecimalFormatter implements Formatter<BigDecimal>{
	
	private DecimalFormat decimalFormat;

	public BigDecimalFormatter (String pattern) {
		NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
		decimalFormat = (DecimalFormat) format;
		decimalFormat.setParseBigDecimal(true);
		decimalFormat.applyPattern(pattern);
		
	}
	/**
	 * Recebe um OBJETO da tela e RETORNA um VALOR (String)
	 */
	@Override
	public String print(BigDecimal object, Locale locale) {
		return decimalFormat.format(object);
	}

	/**
	 * Recebe um VALOR(String) da tela e RETORNA um OBJETO
	 */
	@Override
	public BigDecimal parse(String text, Locale locale) throws ParseException {
		return (BigDecimal) decimalFormat.parse(text);
	}

	

}
