package com.smart.controller;

import javax.servlet.http.HttpSession;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.config.MyConfig;

@Controller
@RequestMapping

public class HomeController {
	
	// home page chalega to yahan ek handler chalega home controller
	// yhan se home page return karega
	
	// home ke liye
	
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/")  
	
	public String home(Model model){
		model.addAttribute("title"," Home Smart Contact Manager ");
		
		return "home.html";
	}
	
	
	// about page ke liye
	
	@RequestMapping("/about") 
	
	public String about(Model model){
		model.addAttribute("title"," About Smart Contact Manager ");
		
		return "about.html";
	}
	
	// signup page ke liye
	
	@RequestMapping("/signup") 
	
	public String signup(Model model){
		model.addAttribute("title"," Register - Smart Contact Manager ");
		model.addAttribute("user", new User());
		
		return "signup.html";
	}
	
	// handler for custom login page 
	
	@GetMapping("/signin")
	public String customLogin(Model model) {
		
		model.addAttribute("title"," Login - Smart Contact Manager ");
		return "login.html";
		
	}

	
	
	//  below handler is for registering user 
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value="agreement",defaultValue="false")boolean agreement,Model model,HttpSession session){
		
		try {
			
			if(agreement==false) {
				System.out.println("Sir you didn't checked terms and conditions");
				throw new Exception("Sir you didn't checked terms and condition"); // ye exception ddega
			}
			if(result1.hasErrors()) {
				System.out.println(" ERROR "+result1.toString());
				model.addAttribute("user",user);
				return "signup.html";
			}
			
			
			
			
			// jo jo bache fields hain user ki usko fill krdo 
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("Agreement "+agreement);
			System.out.println("USER "+user);
			
			User result=this.userRepository.save(user);
			
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message(" Succesfully registered !! ","alert-success"));
			
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message(" Something went wrong !! "+e.getMessage(),"alert-danger"));
			
		}
		
		return "signup.html";
		
	}
	
	
	
	// ye check krne ke liye tha biidu ki data jaa rha hai ya nhi
//	@Autowired
//	private UserRepository userRepository;
//	
//	@GetMapping("/test") // url dalte hain getmapping ke under btane keliye ki kispe chala rha
//	@ResponseBody // jo response bhej rha use return krna hai to hm use krte hain Responsebody
//	
//	
//	public String test() {
//		
//		User user =new User();
//		user.setName("Atul sharma ji");
//		user.setEmail("atulsharma98535@gmail1.com");
//		
//		Contact contact=new Contact();
//		user.getContacts().add(contact);
//		
//		userRepository.save(user);
 //      return "Working";
		
//	}

}

