package com.rlsp.cervejaria.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.rlsp.cervejaria.mail.Mailer;

/**
 * @ComponentScan(basePackageClasses = Mailer.class)
 *  - o Spring precisa Scanear o componente na classe "Mailer" para faze-lo componente do Spring
 */


/**
 * @PropertySource({ "classpath:env/mail-${ambiente:local}.properties" })
 *  - Carrega os dados de USERNAME + PASSWORD para envio do Email
 *  - ambiente:local ==> busca em "ambiente", caso contrario pega o default que será "local"
 * - deve ser configurado no SERVIDOR WEB (TomCat ou outro) a variavel "ambiente"
 *   a) Open Launch configuration --> Enviroment (tab) --> Add (nesse caso ==> name : ambiente; value : production
 */


/**
 * @PropertySource(value = { "file://${HOME}/.cervejaria-mail.properties" }, ignoreResourceNotFound = true)
 *  - Carrega do Arquivo abaixo no Servidor 
 *  - Sobrescreve as configuracoes acima se sobrescrever
 *  - "ignoreResourceNotFound = true" ignora o ERRO por falta do arquivo "file://${HOME}/.cervejaria-mail.properties" 
 */

@Configuration
@ComponentScan(basePackageClasses = Mailer.class)
@PropertySource({ "classpath:env/mail-${ambiente:local}.properties" })
@PropertySource(value = { "file://${HOME}/.cervejaria-mail.properties" }, ignoreResourceNotFound = true)
public class MailConfig {
	
	@Autowired
	private Environment env; // carrega o @PropertySource aqui dentro

	/**
	 * Usando "Sender Grid" Framework
	 * 
	 */
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.sendgrid.net"); // 
		mailSender.setPort(587);
		
		mailSender.setUsername(env.getProperty("email.username"));
		mailSender.setPassword(env.getProperty("email.password"));

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.debug", false);
		props.put("mail.smtp.connectiontimeout", 10000); // miliseconds

		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}
	
}
