package com.rlsp.cervejaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
@EnableWebSecurity // Habilita a Seguranca WEB
public class SecurityConfig extends WebSecurityConfigurerAdapter{


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication() //Para testar em MEMORIA
			.withUser("admin").password("admin").roles("CADASTRO_CLIENTE");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated() // Qualquer requisicao DEVER AUTENTICAR
				.and()
			.formLogin()					 //
				.and()
			.csrf().disable();
		
		/*
		 * csrf
		 */
	}
	
	
	/**
	 * Implementacao usando BCrypt (Criptografia da senha do usuario) 
	 */


	@Bean
	public PasswordEncoder bcryptPasswordEncoder() 
	{
	    return new BCryptPasswordEncoder();
	}


	
	
}
