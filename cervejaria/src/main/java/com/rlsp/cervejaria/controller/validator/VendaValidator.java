package com.rlsp.cervejaria.controller.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.rlsp.cervejaria.model.Venda;



@Component
public class VendaValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Venda.class.isAssignableFrom(clazz); // Diz que para o SPRING VALIDATOR implementar essa Classe "model/Venda"
	}

	/**
	 * Implementacao da VALIDACAO em "model/Venda"
	 *  - target : é o OBJETO de VENDA
	 */
	@Override
	public void validate(Object target, Errors errors) {
		/**
		 * è um como se fosse um Bean Validation
		 *  - Valida o CODIGO do Cliente (se esta vazio)
		 */
		ValidationUtils.rejectIfEmpty(errors, "cliente.codigo", "", "Selecione um cliente na pesquisa rápida");
		
		Venda venda = (Venda) target;
		validarSeInformouApenasHorarioEntrega(errors, venda); // Valida Horario de ENTEREGA
		validarSeInformouItens(errors, venda); // Valida se informou PELO MENOS 1 ITEM
		validarValorTotalNegativo(errors, venda); // Valida se o valor Total é MENOR QUE ZERO
	}

	private void validarValorTotalNegativo(Errors errors, Venda venda) {
		if (venda.getValorTotal().compareTo(BigDecimal.ZERO) < 0) {
			errors.reject("", "Valor total não pode ser negativo");
		}
	}

	private void validarSeInformouItens(Errors errors, Venda venda) {
		if (venda.getItens().isEmpty()) {
			errors.reject("", "Adicione pelo menos uma cerveja na venda");
		}
	}

	private void validarSeInformouApenasHorarioEntrega(Errors errors, Venda venda) {
		
		// Se INFORMA HORARIO de ENTREGA e NAO INFORMAR a Data de Entrega
		if (venda.getHorarioEntrega() != null && venda.getDataEntrega() == null) {
			errors.rejectValue("dataEntrega", "", "Informe uma data da entrega para um horário");
		}
	}

}