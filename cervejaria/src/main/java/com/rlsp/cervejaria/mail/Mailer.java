package com.rlsp.cervejaria.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.rlsp.cervejaria.model.Venda;



@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;
		
	@Async
	public void enviar(Venda venda) {
		SimpleMailMessage mensagem = new SimpleMailMessage();
		mensagem.setFrom("rlsprojects.ca@gmail.com");
		mensagem.setTo(venda.getCliente().getEmail());
		System.out.println(">>> Email do cliente : " + venda.getCliente().getEmail());
		mensagem.setSubject("Venda Efetuada");
		mensagem.setText("Obrigado, sua venda foi processada!");
		
		mailSender.send(mensagem);
	}
	
}
