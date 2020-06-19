package com.rlsp.cervejaria.mail;


import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.ItemVenda;
import com.rlsp.cervejaria.model.Venda;
import com.rlsp.cervejaria.storage.FotoStorage;




@Component
public class Mailer {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(Mailer.class);
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf; // Criado ao cadastrar a classe MAILER.java no WebConfig, para entao, poder usar o thymeleaf nos EMAILs
		
	@Autowired
	private FotoStorage fotoStorage;
	
	@Async
	public void enviar(Venda venda) {
//		SimpleMailMessage mensagem = new SimpleMailMessage();
//		mensagem.setFrom("rlsprojects.ca@gmail.com");
//		mensagem.setTo(venda.getCliente().getEmail());
//		System.out.println(">>> Email do cliente : " + venda.getCliente().getEmail());
//		mensagem.setSubject("Venda Efetuada");
//		mensagem.setText("Obrigado, sua venda foi processada!");
//		
//		mailSender.send(mensagem);
		

		Context context = new Context(); // Cria um contexto Web do Thymeleaf
		context.setVariable("venda", venda); // Set "venda" para o contexto, para ser usado no EMAIL
		context.setVariable("logo", "logo"); // 
		
		Map<String, String> fotos = new HashMap<>();
		boolean adicionarMockCerveja = false;
		
		/**
		 * Verifica se a cerveja TEM foto para ser colocada como anexo para ser usado no EMAIL
		 * - feito ANTES do PROCESSAMENTO do Thymeleaf
		 * - "cid" : cerveja Id
		 */
		for (ItemVenda item : venda.getItens()) { // Percorrer todos ITENS de VENDA, para ver se TEM FOTO
			Cerveja cerveja = item.getCerveja();
			//if(!StringUtils.isEmpty(cerveja.getFoto()))
			if (cerveja.temFoto()) {
				String cid = "foto-" + cerveja.getCodigo(); // Gera um nome para cada uma das fotos (para ser incluido no contexto)
				context.setVariable(cid, cid); // Set o cid (foto) para ser usado o Email (COM FOTO)
				
				//cid : chave / foto+content Type : valor
				fotos.put(cid, cerveja.getFoto() + "|" + cerveja.getContentType()); // Salva o (nome da foto) + (content type da foto) no MAP
			} else {
				adicionarMockCerveja = true;
				context.setVariable("mockCerveja", "mockCerveja"); // Set o a cerveja SEM FOTO
			}
		}
		
		try {
			String email = thymeleaf.process("mail/ResumoVenda", context); // Passa as informacoes do CONTEXTO para Thymeleaf que retorna o TEXTO DO EMAIL
			
			MimeMessage mimeMessage = mailSender.createMimeMessage(); // Cria a MENSAGEM que contem HTML
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // ((mimeMessage, multipart, encoding); multipart = pq sera adicionado IMAGENS
			helper.setFrom("rlsprojects.ca@gmail.com");
			helper.setTo(venda.getCliente().getEmail());
			helper.setSubject("Cervejaria - Venda realizada");
			helper.setText(email, true); // true = pq é um email HTML
			
			helper.addInline("logo", new ClassPathResource("static/images/logo-gray.png")); // Adiciona o LOGO com anexo do EMAIL
			
			if (adicionarMockCerveja) { // Apenas adiciona se a cerveja NAO TIVER foto
				helper.addInline("mockCerveja", new ClassPathResource("static/images/cerveja-mock.png"));
			}
			

			for (String cid : fotos.keySet()) {
				String[] fotoContentType = fotos.get(cid).split("\\|"); // Desmebra (nome da Foto) + (content type da foto)
				String foto = fotoContentType[0];
				String contentType = fotoContentType[1];
				byte[] arrayFoto = fotoStorage.recuperarThumbnail(foto); // Recuper o  THUMBNAIL da Cerveja
				helper.addInline(cid, new ByteArrayResource(arrayFoto), contentType); // Adiciona a foto da Cerveja no EMAIL
			}
		
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error(">>> Erro enviando e-mail <<<", e);
		}
	}
	
}
