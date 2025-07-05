package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BookDto;
import com.example.exception.MissingTokenException;
import com.example.security.JwtUtil;
import com.example.service.IBookService;
import com.example.service.impl.BookServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	@Autowired
	private IBookService bookService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	
	@GetMapping
	public List<BookDto> getAllBooks(){
		return bookService.getAllBooks();
	}
	
	@GetMapping("/owner/{username}")
	public List<BookDto> getBooksByUsername(@PathVariable("username") String username, HttpServletRequest request){
		String token = getAuthHeaderOrThrowException(request);
		return bookService.getBooksByUsername(username);
	}
	
	@GetMapping("/{bookId}")
	public BookDto getBookById(@PathVariable("bookId") String bookId, HttpServletRequest request) {
		getAuthHeaderOrThrowException(request);
		return bookService.findBookById(Long.valueOf(bookId));
	}
	
	
	@PostMapping("/save")
	public BookDto addBook(@RequestBody BookDto bookDto, HttpServletRequest request) {
		String token = getAuthHeaderOrThrowException(request);
		String username = jwtUtil.extractUsername(token);
		return bookService.save(bookDto, username);
	}
	
	@DeleteMapping("/delete/{bookId}")
	public void deleteBook(@PathVariable("bookId") String bookId, HttpServletRequest request) {
		String token = getAuthHeaderOrThrowException(request);
		String username = jwtUtil.extractUsername(token);
		bookService.deleteBook(Long.valueOf(bookId), username);
	}
	
	@PutMapping("/update/{bookId}")
	public BookDto updateBook(@PathVariable("bookId") String bookId, @RequestBody BookDto newBookDto, HttpServletRequest request) {
		String token = getAuthHeaderOrThrowException(request);
		String username = jwtUtil.extractUsername(token);
		
		return bookService.updateBook(Long.valueOf(bookId), newBookDto, username);
	}
	
	// helper function for repetitive things
	private String getAuthHeaderOrThrowException(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.warn("{} Authorization header eksik veya geçersiz.", authHeader);
			throw new MissingTokenException("Authorization header eksik veya geçersiz");
		}
		return authHeader.substring(7);
	}
}
