package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.UserDto;
import com.example.service.IAuthService;
import com.example.service.IUserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private IAuthService authService;
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/register")
	public UserDto register(@RequestBody UserDto userDto) {
		return authService.register(userDto);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody UserDto userDto) {
		return authService.login(userDto);
	}	

}
