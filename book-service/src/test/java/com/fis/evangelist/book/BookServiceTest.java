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

import com.fis.evangelist.book.entity.Book;
import com.fis.evangelist.book.repository.BookRepository;
import com.fis.evangelist.book.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	@Mock private BookRepository bookRepository;
	
	@InjectMocks
    private BookService bookService;
	
	private Book book;
	private List<Book> books;
	
	@BeforeEach
    public void setup(){
		this.book = new Book();
		this.book.setId(1L);
		this.book.setBookId("B1212");
		this.book.setName("History of Amazon Valley");
		this.book.setAuthor("Ross Suarez");
		this.book.setCopiesAvailable(2);
		this.book.setTotalCopies(2);
		
		Mockito.lenient().when(this.bookRepository.save(book))
        .thenReturn(this.book);
		
		this.books = new ArrayList<Book>();
		this.books.add(this.book);
		
		Mockito.lenient().when(this.bookRepository.findAll())
        .thenReturn(this.books);
		
		Mockito.lenient().when(this.bookRepository.findByBookId("B1212"))
        .thenReturn(this.book);
    }
	
	@Test void saveBook() {
		Book book = this.bookService.saveBook(this.book);
		assertThat(book).isNotNull();
	}
	
	@Test void getAllBooks() {
		List<Book> books = this.bookService.getAllBooks();
		assertThat(books.size()).isGreaterThan(0);
	}
	
	@Test void getBook() {
		Book book = this.bookService.getBook("B1212");
		assertThat(book).isNotNull();
	}
	
	@Test void getBookThatNotExists() {
		Book book = this.bookService.getBook("B12122");
		assertThat(book).isNull();
	}
}
