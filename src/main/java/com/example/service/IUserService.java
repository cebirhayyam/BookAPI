package com.example.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.dto.UserDto;
import com.example.entity.User;

public interface IUserService {
	
	public UserDto save(User User);
	
	public User findByUsername(String username);
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	public boolean existsByUsername(String username);

}
