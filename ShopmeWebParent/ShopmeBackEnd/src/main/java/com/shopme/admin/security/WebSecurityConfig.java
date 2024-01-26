package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder PasswordEncoder() {
		
		return new BCryptPasswordEncoder(); 
	}
	
	
	
	@Bean
	public SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
		
		
		http.authorizeHttpRequests((authz) -> authz
				
				.anyRequest().authenticated()
				
				).formLogin( form -> form 
						.loginPage("/login")
						.usernameParameter("email")
						.permitAll()); 
				
				
					
				
		
		return http.build(); 
		
		
	}
	
	@Bean
	WebSecurityCustomizer configureWebSecurity() throws Exception {
		
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**"); 
	}

}
