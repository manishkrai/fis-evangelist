package com.fis.evangelist.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fis.evangelist.book.DuplicateBookException;
import com.fis.evangelist.book.entity.Book;
import com.fis.evangelist.book.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	public Book saveBook(Book book) throws DuplicateBookException {
		log.info("Inside saveBook method of BookService");
		if(book.getId() == null) {
			Book availableBook= bookRepository.findByBookId(book.getBookId());
			if(availableBook != null) {
				throw new DuplicateBookException("Book is already added.");
			}
		}
		return bookRepository.save(book);
	}
	
	public List<Book> getAllBooks() {
		log.info("Inside getBooks method of BookService");
		return bookRepository.findAll();		
	}
	
	public Book getBook(String bookId) {
		log.info("Inside getBook method of BookService");
		return bookRepository.findByBookId(bookId);
	}
}
