package com.fis.evangelist.book;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.evangelist.book.controller.BookController;
import com.fis.evangelist.book.entity.Book;
import com.fis.evangelist.book.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
	
	@InjectMocks
	private BookController bookController;
	
	@Mock
    private BookService bookService;
	
	private MockMvc mockMvc;
	
	private Book book;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@BeforeEach
    public void setup(){
		this.book = new Book();
		this.book.setId(1L);
		this.book.setBookId("B1212");
		this.book.setName("History of Amazon Valley");
		this.book.setAuthor("Ross Suarez");
		this.book.setCopiesAvailable(2);
		this.book.setTotalCopies(2);
		
		this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }
	
	@Test 
	void saveBook() throws Exception {	
		Mockito.when(bookService.saveBook(ArgumentMatchers.any())).thenReturn(this.book);		
		
		 String json = mapper.writeValueAsString(this.book);
        mockMvc.perform(post("/books/saveBook")
        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isCreated())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$.bookId").value("B1212"))
        		.andExpect(jsonPath("$.name").value("History of Amazon Valley"));
	}
	
	@Test 
	void getBooks() throws Exception {	
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		
		Mockito.when(bookService.getAllBooks()).thenReturn(books);		
		
		 String json = mapper.writeValueAsString(this.book);
        mockMvc.perform(get("/books")
                .content(json).accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[*].bookId").value("B1212"))
        		.andExpect(jsonPath("$[*].name").value("History of Amazon Valley"));
	}
	
	@Test 
	void getBook() throws Exception {	
		Mockito.when(bookService.getBook(ArgumentMatchers.any())).thenReturn(this.book);
        mockMvc.perform(get("/books/getBook/{bookId}", "B1212")
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$.bookId").value("B1212"))
        		.andExpect(jsonPath("$.name").value("History of Amazon Valley"));
	        		
	}

	
}
