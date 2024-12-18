package com.project.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.dao.entitycontact;
import com.project.dao.entitydao;
import com.project.entity.contact;
import com.project.entity.user;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class User_Controller {

	@Autowired
	private entitydao entitydao;
	
	@Autowired
	private entitycontact entitycontact;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// For Common data method

	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {

		String username = principal.getName();
		System.out.println(username);

		user user = entitydao.getuserbyname(username);

		m.addAttribute("user", user);

	}

	@GetMapping("/index")
	public String dashboard(Model m) {

		m.addAttribute("title", "User dashboard");

		return ("/normal/index");
	}

	@GetMapping("/add_contact")
	public String addcontact(Model m) {

		m.addAttribute("title", "Add contact");
		m.addAttribute("contact", new contact());
		return "/normal/add";

	}

	@PostMapping("/process-contact")
	public String processcontact(@ModelAttribute contact contact ,@RequestParam("profileimage") MultipartFile file, Principal principal) 
	{

		
		try {
			String name = principal.getName(); 	
			user user = this.entitydao.getuserbyname(name);
			
			
			if(file.isEmpty()) {
				System.out.println("file is empty");
				contact.setImage("contact.png");
			}
			else {
				
				contact.setImage(file.getOriginalFilename());
				
				File savefile = new ClassPathResource("static/profile_image").getFile();
				
				Path path=Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}
			
			user.getContact().add(contact);
			contact.setUser(user);
			this.entitydao.save(user);

			System.out.println(contact);
		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "/normal/add";

	}
	
	@GetMapping("/show_contact")
	public String showcontact(Model m,Principal principal) {
		
		String username = principal.getName();
		
		user user = this.entitydao.getuserbyname(username);
		
		List<contact> contacts = this.entitycontact.findcontactByuser(user.getId());
		
		m.addAttribute("contacts",contacts);
		
		return "/normal/show_contact";
	}
	
	@GetMapping("user/delete/{cid}")
	public String deletecontact(@PathVariable("cid") Integer cid)
	{
		//this.entitycontact.deleteById(cid);
		System.out.println(cid);
		Optional<contact> optionalId = this.entitycontact.findById(cid);
		
		contact contact = optionalId.get();
		
		
		
		this.entitycontact.delete(contact);
		
		return "redirect:/user/show_contact";
		
	}
	
	@GetMapping("/setting")
	public String setting() {
		
		return "/normal/setting";
		
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam String oldpassword,@RequestParam String newpassword,Principal principal,HttpSession session) {
		
		System.out.println("oldpassword "+oldpassword);
		System.out.println("new password "+newpassword);
		
		String username = principal.getName();
		user currentuser = this.entitydao.getuserbyname(username);
		System.out.println(currentuser.getPassword());
		if(this.bCryptPasswordEncoder.matches(oldpassword, currentuser.getPassword())){
			
			currentuser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.entitydao.save(currentuser);
			session.setAttribute("message", "password change succesfully");
			

		}
		else {
			session.setAttribute("message", "password not matched");
			
			return "/normal/setting";
		}
		return "/normal/index";
	}
	
}
