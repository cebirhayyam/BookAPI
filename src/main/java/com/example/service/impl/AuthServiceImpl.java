package com.example.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.InvalidLoginException;
import com.example.exception.UserAlreadyExistsException;
import com.example.security.JwtUtil;
import com.example.service.IAuthService;
import com.example.service.IUserService;

@Service
public class AuthServiceImpl implements IAuthService{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@Override
	public UserDto register(UserDto userDto) {
		if (userService.existsByUsername(userDto.getUsername())) {
			logger.warn("{} adlı kullanıcı tekrardan kayıt olmaya çalıştı.");
			throw new UserAlreadyExistsException("Kullanıcı zaten kayıtlı: " + userDto.getUsername());
		}
		
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setPassword(userDto.getPassword());
		user.setBooks(new ArrayList<>());
		
		return userService.save(user);
	}

	@Override
	public String login(UserDto userDto) {
		User user = userService.findByUsername(userDto.getUsername());
		
		if (user == null) {
			logger.warn("Giriş denemesi başarısız! {} adlı kullanıcı bulunamadı.", userDto.getUsername());
			throw new InvalidLoginException("Geçersiz kullanıcı adı veya şifre");
		}
		
		
		if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
			logger.warn("Giriş denemesi başarısız! {} adlı kullanıcı yanlış şifre girdi.", userDto.getUsername());
			throw new InvalidLoginException("Geçersiz kullanıcı adı veya şifre");
		}
		
		logger.info("'{}' uygulamaya başarıyla giriş yaptı.", userDto.getUsername());
		return jwtUtil.generateToken(user);
	}

}
