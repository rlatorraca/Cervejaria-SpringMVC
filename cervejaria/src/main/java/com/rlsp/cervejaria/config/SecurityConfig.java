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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

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
		/**
		 * Autenticacao usando o DB e CRIPTOGRAFIA
		 */
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
		/**
		 * Sempre COMECAR com o que se quer BLOQUEAR
		 */
		http			
		//.csrf().disable();
			.httpBasic()
		 	.and()
			.authorizeRequests()
				.antMatchers("/cidades/nova").hasRole("CADASTRAR_CIDADE")  
				.antMatchers("/usuarios/**").hasRole("CADASTRAR_USUARIO")
				//.antMatchers("/layout/**").permitAll()   // Tudo que tiver em dentro da pasta LAYOUT pode ser acessado sem AUTENTICACAO	
				//.antMatchers("/images/**").permitAll()   // Tudo que tiver em dentro da pasta IMAGES pode ser acessado sem AUTENTICACAO
				.anyRequest().authenticated() // Qualquer requisicao DEVER AUTENTICAR	
				//.anyRequest().denyAll() 	  // Para NEGAR tudo, mas como ja esta bloqueando se nao for autenticado nao precisa	
				.and()			
			.formLogin()
				.loginPage("/login")          // Define a localizacao da pagina de LOGIN
				.permitAll()				  // Nao precisa estar autenticado
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  // Logout com CSRF "th:action"
				.and()
			.sessionManagement()
				.invalidSessionUrl("/login")   //Em caso de NAO estar logado e tenhta submeter algo
			    .and()
			//	.maximumSessions(1)    // 1 Sessao por usuario
			//	.expiredUrl("/login"); // Retorna para pagina de Login (ao expirar a sessaco)
			.exceptionHandling()
				.accessDeniedPage("/403");
				

				
			
			//.sessionManagement()
			//	.maximumSessions(1)    // 1 Sessao por usuario
			//	.expiredUrl("/login"); // Retorna para pagina de Login (ao expirar a sessaco)
				
			
		
		/*
		 * csrf ==> Cross-site Request Forgery
		 * - Enviar um PAGINA externa com proposito de ATAQUE ao site
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
