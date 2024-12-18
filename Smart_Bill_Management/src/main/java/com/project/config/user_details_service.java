package com.project.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.project.dao.entitydao;
import com.project.entity.user;

@Component
public class user_details_service implements UserDetailsService{

	@Autowired
	private entitydao entitydao;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		user user= entitydao.getuserbyname(email);
		
		if(user==null) {
			
			throw new UsernameNotFoundException("User not found");
			
		}
		
		return new user_detail(user);
	}
	
	

}
