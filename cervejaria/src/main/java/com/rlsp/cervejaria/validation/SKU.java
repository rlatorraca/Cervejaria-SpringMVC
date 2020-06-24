package com.rlsp.cervejaria.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * Validacao BEAN VALIDATION
 * @Target ==> onde pode ser aplicacada
 * @Retention(RetentionPolicy.RUNTIME) == avaliada em tempo de execucao
 *
 */

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "([a-zA-Z]{2}\\d{4})?") // Letras Maisculas ou Minusculas de a-Z (limitaados a 2 letras) + 4 digitos ? = aplica apenas se tiver dados
public @interface SKU {

	/**
	 * Sobrescreve o a mensagem padrao do "pattern" 
	 */
	@OverridesAttribute(constraint = Pattern.class, name = "message")
	//String message() default "SKU deve seguir o padrão XX9999";
	String message() default "{com.rlsp.cervejaria.constraints.SKU.message}";
	
	
	Class<?>[] groups() default {}; // Para agrupar validacoes
	Class<? extends Payload>[] payload() default {}; // pode carregar mais informacoes
	
}
