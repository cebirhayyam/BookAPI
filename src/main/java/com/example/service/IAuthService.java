package com.example.service;

import com.example.dto.UserDto;
import com.example.entity.User;

public interface IAuthService {
	
	public UserDto register(UserDto userDto);
	
	public String login(UserDto userDto);

}
