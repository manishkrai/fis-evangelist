package com.fis.evangelist.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fis.evangelist.book.entity.Book;
import com.fis.evangelist.book.service.BookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@PostMapping("/saveBook")
	public Book saveBook(@RequestBody Book book) {
		log.info("Inside saveBook method of BookController");
		return bookService.saveBook(book);		
	}
	
	@GetMapping("")
	public List<Book> getBooks() {
		log.info("Inside getAllBooks method of BookController");
		return bookService.getAllBooks();		
	}
	
	@GetMapping("/getBook/{bookId}")
	public Book getBook(@PathVariable("bookId") String bookId) {
		log.info("Inside getBook method of BookController");
		return bookService.getBook(bookId);		
	}
}
