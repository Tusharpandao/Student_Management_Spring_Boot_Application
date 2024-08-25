package com.techeazy.studentmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techeazy.studentmanagement.entity.Users;
import com.techeazy.studentmanagement.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService service;
	
	@PostMapping("/register")
	public Users register(@RequestBody Users user) {
		
		Users newUser=service.register(user);
	return newUser; 
	}
	

	
	
	@PostMapping("/login")
	public String login(@RequestBody Users user) {
		return service.verify(user);
	}

}
