package com.example.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.BookNotFoundException;
import com.example.exception.InvalidLoginException;
import com.example.exception.MissingTokenException;
import com.example.exception.UnauthorizedActionException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BookNotFoundException.class)
	public String handleBookNotFoundException(BookNotFoundException exception) {
		return "[" + exception.getMessage() + "]";
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public String handleUserNotFoundException(UserNotFoundException exception) {
		return "[" + exception.getMessage() + "]";
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public String handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
		return "[" + exception.getMessage() + "]";
	}
	
	@ExceptionHandler(InvalidLoginException.class)
	public String handleInvalidLoginException(InvalidLoginException exception) {
		return "[" + exception.getMessage() + "]";
	}
	
	@ExceptionHandler(MissingTokenException.class)
		public String handleMissingTokenException(MissingTokenException exception) {
			return "[" + exception.getMessage() + "]";
	}
	
	@ExceptionHandler(UnauthorizedActionException.class)
		public String handleUnauthorizedActionException(UnauthorizedActionException exception) {
			return "[" + exception.getMessage() + "]";
	}
	
	@ExceptionHandler(Exception.class)
	public String otherAllException(Exception exception) {
		return "[Exception Mesajı: " + exception.getMessage() + "]";
	}

}
