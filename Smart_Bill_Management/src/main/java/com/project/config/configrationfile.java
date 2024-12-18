package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class configrationfile {

    @Bean
    public BCryptPasswordEncoder getpasswordEncoder() {
		
		return new BCryptPasswordEncoder();
		
	}
    @Bean
	public UserDetailsService getservice() {
		
		return new user_details_service();
		
	}
	
    @Bean
    public DaoAuthenticationProvider daoauthproProvider() {
    	
    	DaoAuthenticationProvider daoProvider =new DaoAuthenticationProvider();
    	
    	daoProvider.setUserDetailsService(getservice());
    	
    	daoProvider.setPasswordEncoder(getpasswordEncoder());
    	
    	return daoProvider;
    }
    
    @Bean
    public SecurityFilterChain securityfilChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> {
			try {
                requests.
                        requestMatchers("/home").permitAll().
                        requestMatchers("/signup").permitAll().
                        requestMatchers("/about").permitAll().
                        
                        requestMatchers("/do-register").permitAll()
                        
                        .anyRequest().authenticated()
                        .and().formLogin(login -> login.loginPage("/signin").permitAll().
                        		loginProcessingUrl("/do-login").permitAll().
                        		defaultSuccessUrl("/user/index",true).permitAll());
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
                );
    
    	
    	
    	
    	
    	return http.build();
    	
    }
}
