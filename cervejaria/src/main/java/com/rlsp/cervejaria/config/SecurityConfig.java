package com.rlsp.cervejaria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rlsp.cervejaria.security.AppUserDetailsService;

@Configuration
@EnableWebSecurity // Habilita a Seguranca WEB
@ComponentScan(basePackageClasses = AppUserDetailsService.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Autowired
	UserDetailsService userDetailsService;
	

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception{
    	auth.inMemoryAuthentication()
		.withUser("admin").password("admin").roles("admin");
    	
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("admin").password("admin").roles("admin");
		auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	
		super.configure(web);
		web.ignoring()
			.antMatchers("/layout/**")   // Tudo que tiver em dentro da pasta LAYOUT pode ser acessado sem AUTENTICACAO
			.antMatchers("/images/**");  // Tudo que tiver em dentro da pasta IMAGES pode ser acessado sem AUTENTICACAO
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.httpBasic()
		 	.and()
			.authorizeRequests()
				//.antMatchers("/layout/**").permitAll()   // Tudo que tiver em dentro da pasta LAYOUT pode ser acessado sem AUTENTICACAO	
				//.antMatchers("/images/**").permitAll()   // Tudo que tiver em dentro da pasta IMAGES pode ser acessado sem AUTENTICACAO
				.anyRequest().authenticated() // Qualquer requisicao DEVER AUTENTICAR				
				.and()
			
			.formLogin()
				.loginPage("/login")          // Define a localizacao da pagina de LOGIN
				.permitAll();				  // Nao precisa estar autenticado
				
			
			
		
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
