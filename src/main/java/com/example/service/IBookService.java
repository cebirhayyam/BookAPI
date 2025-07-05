package com.example.service;

import java.util.List;

import com.example.dto.BookDto;

public interface IBookService {
	
	public BookDto save(BookDto bookDto, String usename);
	
	public List<BookDto> getAllBooks();
	
	public List<BookDto> getBooksByUsername(String username);
	
	public void deleteBook(Long id, String username);
	
	public BookDto findBookById(Long id);
	
	public BookDto updateBook(Long id, BookDto bookDto, String username);
	
}
