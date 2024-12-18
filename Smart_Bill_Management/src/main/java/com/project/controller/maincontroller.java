package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dao.entitydao;
import com.project.entity.user;


@Controller
public class maincontroller {
	
	@Autowired
	private entitydao entitydao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/home")
	public String home(Model m) {
		m.addAttribute("title","this is home-page");
		
		return "/home";
	}
	
	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","This is about page");
		
		return "about";
	}
	
	@GetMapping("/signin")
	public String login(Model m) {
		m.addAttribute("title","this is login-page");
		
		return "signin";
	}
	
	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title","This is signup page");
		m.addAttribute("user",new user());
		return "signup";
	}
	
	@PostMapping("/do-register")
	public String signuphandler(@ModelAttribute user user,Model m) {
		user.setEnabled(true);
		user.setRole("Normal User");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user result = entitydao.save(user);
		System.out.println(result);
		
		return "signup";
	}
	
}
