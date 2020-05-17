package com.rlsp.cervejaria.controller.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rlsp.cervejaria.service.exception.NomeEstiloJaCadastradoException;

/**
 * Controller Advice
 *  - Serve para ficar monitorando ERROR / EXCEPTION que ocorrencia juntos aos CONTROLLER e caso elas NAO TENHA sido tratadas esta classe fara o TRATAMENTo em enviara as mensagens de ERRO / EXCEPTION
 *  - Muito Usando em caso de respostas simples em partes com AJAX +JAVA
 */

@ControllerAdvice
public class ControllerAdviceExceptionHandler {

	
	@ExceptionHandler(NomeEstiloJaCadastradoException.class)
	public ResponseEntity<?> handleNomeEstiloJaCadastradoException(NomeEstiloJaCadastradoException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
