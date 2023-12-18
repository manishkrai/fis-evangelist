package com.fis.evangelist.book.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fis.evangelist.book.exception.BookNotFoundException;
import com.fis.evangelist.book.exception.DuplicateBookException;
import com.fis.evangelist.model.BookRequest;
import com.fis.evangelist.model.BookResponse;

public interface BookService {
	
	BookResponse saveBook(BookRequest bookRequest) throws DuplicateBookException;
	List<BookResponse> getAllBooks();
	BookResponse getBook(String bookId) throws BookNotFoundException;

}
