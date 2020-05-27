package com.rlsp.cervejaria.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.apache.commons.beanutils.BeanUtils;

import com.rlsp.cervejaria.validation.AtributoConfirmacaoSenha;

/**
 * Object ==> pode ser usado em QUALQUER Objeto e NAO APENAS em Usuario (para confirmar senha)
 * @author rlatorraca
 *
 */
public class AtributoConfirmacaoSenhaValidator implements ConstraintValidator<AtributoConfirmacaoSenha, Object>{

	private String atributoSenha;
	
	private String atributoConfirmacaoSenha;
	
	@Override
	public void initialize(AtributoConfirmacaoSenha constraintAnnotation) {
		this.atributoSenha = constraintAnnotation.atributoSenha();
		this.atributoConfirmacaoSenha = constraintAnnotation.atributoConfirmacaoSenha();
	}
	
	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		boolean valoresValidos = false;
		try {
			Object valorSenha = BeanUtils.getProperty(object, this.atributoSenha); // Pega o valor da SENHA
			Object valorSenhaConfirmacao = BeanUtils.getProperty(object, this.atributoConfirmacaoSenha); //Pega o valor da CONTRA-SENHA
			
			valoresValidos = valoresSaoNulos(valorSenha, valorSenhaConfirmacao) || valoresSaoIguais(valorSenha, valorSenhaConfirmacao);
			
			
		} catch (Exception e) {
			throw new RuntimeException("Erro em 'AtributoConfirmacaoSenhaValidator' ao pegar os atributos");

		} 

		/**
		 * Em caso de os valores NAO BATEREM , lança o ERRO
		 */
		if (!valoresValidos) {
			context.disableDefaultConstraintViolation();
			String mensagem = context.getDefaultConstraintMessageTemplate();
			ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(mensagem);		
			
			violationBuilder.addPropertyNode(atributoConfirmacaoSenha).addConstraintViolation();
		}
		
		return valoresValidos;
	}

	private boolean valoresSaoIguais(Object valorSenha, Object valorSenhaConfirmacao) {
		return valorSenha != null && valorSenha.equals(valorSenhaConfirmacao);
	}

	private boolean valoresSaoNulos(Object valorSenha, Object valorSenhaConfirmacao) {
		return valorSenha == null && valorSenhaConfirmacao == null;
	}

	

	
	
}
