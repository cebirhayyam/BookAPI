package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Book;
import com.example.entity.User;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	
	public List<Book> findByUser(User user);

}
