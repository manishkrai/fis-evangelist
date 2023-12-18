package com.fis.evangelist.book.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fis.evangelist.book.entity.Book;
import com.fis.evangelist.book.exception.BookNotFoundException;
import com.fis.evangelist.book.exception.DuplicateBookException;
import com.fis.evangelist.book.mapper.BookMapper;
import com.fis.evangelist.book.repository.BookRepository;
import com.fis.evangelist.book.service.BookService;
import com.fis.evangelist.book.validator.BookRequestValidator;
import com.fis.evangelist.model.BookRequest;
import com.fis.evangelist.model.BookResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
    private final BookMapper bookMapper;

	public BookResponse saveBook(BookRequest bookRequest) throws DuplicateBookException {
		log.info("Saving book with ID: {}", bookRequest.getBookId());

		BookRequestValidator.validate(bookRequest);

		Book availableBook = bookRepository.findByBookId(bookRequest.getBookId());
		if (availableBook != null) {
			throw new DuplicateBookException("Book is already added.");
		}

		Book book = bookMapper.mapToEntity(bookRequest);
		Book updatedBook = bookRepository.save(book);

		BookResponse bookResponse = bookMapper.mapToResponse(updatedBook);
		return bookResponse;
	}

	public List<BookResponse> getAllBooks() {
		log.info("Inside getBooks method of BookService");

		List<Book> allBooks = bookRepository.findAll();
		return allBooks.stream()
	            .map(bookMapper::mapToResponse)
	            .collect(Collectors.toUnmodifiableList());
	}

	public BookResponse getBook(String bookId) throws BookNotFoundException {
		log.info("Getting book with ID: {}", bookId);
		Book book = bookRepository.findByBookId(bookId);
		if(book == null) {
			throw new BookNotFoundException("Book is not available.");
		}
		BookResponse bookResponse = bookMapper.mapToResponse(book);
		return bookResponse;
	}
}
