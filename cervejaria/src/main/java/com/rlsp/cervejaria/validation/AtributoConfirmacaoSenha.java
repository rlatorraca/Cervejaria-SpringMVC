package com.rlsp.cervejaria.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

import com.rlsp.cervejaria.validation.validator.AtributoConfirmacaoSenhaValidator;

@Target({ElementType.TYPE }) // So pode ser usado em CLASSE
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AtributoConfirmacaoSenhaValidator.class})
public @interface AtributoConfirmacaoSenha {

	String atributoSenha();
	
	String atributoConfirmacaoSenha();
			
	/**
	 * Sobrescreve o a mensagem padrao do "pattern" 
	 */
	@OverridesAttribute(constraint = Pattern.class, name = "message")
	String message() default "Senha e Confirmação Senha não conferem";
	
	
	Class<?>[] groups() default {}; // Para agrupar validacoes
	Class<? extends Payload>[] payload() default {}; // pode carregar mais informacoes
}
