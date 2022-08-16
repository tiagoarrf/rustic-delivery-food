package com.trabalho.br.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.trabalho.br.security.UserDetailsServiceImplementacao;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	@Autowired
	private UserDetailsServiceImplementacao userDetaisServiceImple;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable().authorizeRequests()
		
		.antMatchers("/").permitAll()
		.antMatchers("/home").permitAll()
		.antMatchers("/sobre").permitAll()
		.antMatchers("/contato").permitAll()
		.antMatchers("/login").permitAll()
		.antMatchers("/entrar").permitAll()
		.antMatchers("/cliente/atualizar").hasRole("USER")
		.antMatchers("/cliente/salvar").authenticated()
		.antMatchers("/cliente/listar").authenticated()
		.antMatchers("/pedido/listar").authenticated()
		.antMatchers("/pedido/listar/*").authenticated()
		.antMatchers("/pedido/confirmado").authenticated()
		.antMatchers("/pedido/confirmacao").authenticated()
		.antMatchers("/gerente/prato/lisatar").authenticated()
		.antMatchers("/gerente/cliente/listar").authenticated()
		.antMatchers("/pedido/salvar").authenticated()
		.antMatchers("/pessoa/listar").authenticated()
		
		.anyRequest().authenticated()
		
		.and()
		.formLogin()
		.loginPage("/entrar")
		.permitAll()
		
		//.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
		.and()
		.logout()
		.permitAll();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetaisServiceImple).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/css/**", "/js/**","/img/**", "/images/**","/img/*");
	}


}
