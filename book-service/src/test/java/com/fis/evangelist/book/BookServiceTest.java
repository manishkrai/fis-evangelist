package com.fis.evangelist.book;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.fis.evangelist.book.entity.Book;
import com.fis.evangelist.book.exception.BookNotFoundException;
import com.fis.evangelist.book.mapper.BookMapper;
import com.fis.evangelist.book.repository.BookRepository;
import com.fis.evangelist.book.serviceimpl.BookServiceImpl;
import com.fis.evangelist.model.BookRequest;
import com.fis.evangelist.model.BookResponse;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	@Mock 
	private BookRepository bookRepository;
	
	@InjectMocks
    private BookServiceImpl bookService;
	
	@Autowired
	private BookMapper bookMapper;
	
	private BookRequest bookRequest;
	
	private Book book;
	
	@BeforeEach
    public void setup(){
		this.bookRequest = new BookRequest();
		this.bookRequest.setBookId("B1212");
		this.bookRequest.setName("History of Amazon Valley");
		this.bookRequest.setAuthor("Ross Suarez");
		this.bookRequest.setCopiesAvailable(2);
		this.bookRequest.setTotalCopies(2);
		
		this.book = new Book();
		this.book.setBookId("B1212");
		this.book.setName("History of Amazon Valley");
		this.book.setAuthor("Ross Suarez");
		this.book.setCopiesAvailable(2);
		this.book.setTotalCopies(2);
    }
	
	@Test 
	void saveBook() {
		Mockito.lenient().when(this.bookRepository.save(bookMapper.mapToEntity(bookRequest)))
        .thenReturn(this.book);
		try
		{
			BookResponse book = this.bookService.saveBook(bookRequest);
			assertThat(book).isNotNull();
		}
		catch(Exception e)
		{
			
		}
	}
	
	@Test 
	void getAllBooks() {
		List<Book> books = new ArrayList<Book>();
		books.add(this.book);
		
		Mockito.lenient().when(this.bookRepository.findAll())
        .thenReturn(books);
		List<BookResponse> returnedBooks = this.bookService.getAllBooks();
		assertThat(returnedBooks.size()).isGreaterThan(0);
	}
	
	@Test 
	void getBook() throws BookNotFoundException {
		Mockito.lenient().when(this.bookRepository.findByBookId("B1212"))
        .thenReturn(this.book);
		
		BookResponse book = this.bookService.getBook("B1212");
		assertThat(book).isNotNull();
	}
	
	@Test 
	void getBookThatNotExists() throws BookNotFoundException {
		Mockito.lenient().when(this.bookRepository.findByBookId("B1212"))
        .thenReturn(null);
		
		BookResponse book = this.bookService.getBook("B12122");
		assertThat(book).isNull();
	}
}
