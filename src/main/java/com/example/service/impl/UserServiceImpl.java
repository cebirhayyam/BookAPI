package com.example.service.impl;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.example.repository.UserRepository;
import com.example.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	


	@Override
	public User findByUsername(String username) {
		Optional<User> optional = userRepository.findByUsername(username);
		if (optional.isEmpty()) {
			throw new UserNotFoundException("Kullanıcı bulunamadı: " + username);
		}
		return optional.get();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));
		
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.authorities(new ArrayList<>())
				.build();
	}



	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public UserDto save(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		
		User dbUser = new User();
		
		dbUser.setUsername(user.getUsername());
		dbUser.setPassword(encodedPassword);
		dbUser.setBooks(new ArrayList<>());
		
		User savedUser = userRepository.save(dbUser);
		logger.info("'{}' başarılı bir şekilde veritabanına eklendi.", user.getUsername());
		
		UserDto savedUserDto = new UserDto();
		savedUserDto.setUsername(savedUser.getUsername());
		savedUserDto.setPassword("*********************");
		
		return savedUserDto;
	}

}
