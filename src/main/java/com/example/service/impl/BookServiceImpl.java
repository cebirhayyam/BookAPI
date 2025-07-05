package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.BookDto;
import com.example.entity.Book;
import com.example.entity.User;
import com.example.exception.BookNotFoundException;
import com.example.exception.UnauthorizedActionException;
import com.example.exception.UserNotFoundException;
import com.example.repository.BookRepository;
import com.example.repository.UserRepository;
import com.example.service.IBookService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BookServiceImpl implements IBookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	@Override
	public BookDto save(BookDto bookDto, String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + username));

		Book book = new Book();
		book.setTitle(bookDto.getTitle());
		book.setIsbn(bookDto.getIsbn());
		book.setUser(user);
		
		Book savedBook = bookRepository.save(book);
		BookDto savedBookDto = new BookDto();
		
		savedBookDto.setTitle(savedBook.getTitle());
		savedBookDto.setUsername(savedBook.getUser().getUsername());
		savedBookDto.setIsbn(savedBook.getIsbn());
		logger.info("{} username'e sahip kullanıcı, {} adlı bir kitap ekledi.", username, bookDto.getTitle());
		return 	savedBookDto;	
	}

	@Override
	public List<BookDto> getAllBooks() {
		List<Book> books = bookRepository.findAll();
		List<BookDto> bookDtoList = new ArrayList<>();
		
		for (Book book : books) {
			BookDto bookDto = new BookDto();
			bookDto.setTitle(book.getTitle());
			bookDto.setIsbn(book.getIsbn());
			bookDto.setUsername(book.getUser().getUsername());
			bookDtoList.add(bookDto);
		}
		logger.info("Tüm kitaplar görüntülendi.");
		return bookDtoList;
	}

	@Override
	public List<BookDto> getBooksByUsername(String username) {
		List<Book> books = bookRepository.findAll();
		List<BookDto> bookDtoList = new ArrayList<>();
		
		for (Book book : books) {
			if (book.getUser().getUsername().equals(username)) {
				
				BookDto bookDto = new BookDto();
				bookDto.setTitle(book.getTitle());
				bookDto.setIsbn(book.getIsbn());
				bookDto.setUsername(book.getUser().getUsername());
				bookDtoList.add(bookDto);
			}
			
		}
		if (bookDtoList.isEmpty()) {
			throw new BookNotFoundException("Herhangi bir kitap bulunamadı.");
		}
		logger.info("{} isimli kullanıcıya ait tüm kitaplar görüntülendi.", username);
		return bookDtoList;
	}

	@Override
	public void deleteBook(Long id, String username) {
		Optional<Book> optional = bookRepository.findById(id);
		if (optional.isEmpty()) {
			logger.warn("{} id'ye sahip kitap mevcut değil.", id);
			throw new BookNotFoundException("Kitap mevcut değil.");
		}
		
		if (!username.equals(optional.get().getUser().getUsername())) {
			logger.warn("{} adlı username kendisine ait olmayan bir kitabı silmeye çalıştı.", username);
			throw new UnauthorizedActionException("Başka kullanıcıya ait bir kitabı silemezsiniz.");
		}
		
		logger.info("{} id'ye sahip kitap silindi.", id);
		bookRepository.deleteById(id);
	}

	@Override
	public BookDto findBookById(Long bookId) {
		Optional<Book> optional = bookRepository.findById(bookId);
		if (optional.isEmpty()) {
			logger.warn("{} id'ye sahip kitap mevcut değil.", bookId);
			throw new BookNotFoundException("Kitap mevcut değil.");
		}
		
		BookDto bookDto = new BookDto();
		bookDto.setIsbn(optional.get().getIsbn());
		bookDto.setTitle(optional.get().getTitle());
		bookDto.setUsername(optional.get().getUser().getUsername());
		logger.info("{} id'ye sahip kitap görüntülendi.", bookId);
		return bookDto;
	}
	
	@Override
	public BookDto updateBook(Long id, BookDto newBookDto, String username) {
		Optional<Book> optionalBook = bookRepository.findById(id);
		
		if(optionalBook.isEmpty()) {
			logger.warn("{} id'ye sahip kitap mevcut değil.", id);
			throw new BookNotFoundException("Kitap mevcut değil");
		}
		
		String dbBookUsername = optionalBook.get().getUser().getUsername();
		if (!username.equals(dbBookUsername)) {
			logger.warn("{} adlı kullanıcının kendisine ait olmayan bir kitabı güncellemeye çalışması başarısız oldu.", username);
			throw new UnauthorizedActionException("Başka kullanıcıya ait bir kitabı güncelleyemezsiniz");
		}
		
		Book book = optionalBook.get();
		book.setIsbn(newBookDto.getIsbn());
		book.setTitle(newBookDto.getTitle());
		newBookDto.setUsername(dbBookUsername);
		
		bookRepository.save(book);
		
		logger.info("{} id'ye sahip kitap güncellendi.", id);
		return newBookDto;
	}

}
