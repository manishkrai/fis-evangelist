package com.fis.evangelist.book;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.evangelist.book.controller.BookController;
import com.fis.evangelist.book.entity.Book;
import com.fis.evangelist.book.serviceimpl.BookServiceImpl;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ExtendWith(MockitoExtension.class)
public class BookApiRestAssuredTest {
	
	@InjectMocks
	private BookController bookController;
	
	@Mock
    private BookServiceImpl bookService;
	
	private Book book;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@BeforeEach
    public void setup(){
		String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(9001);
        }
        else{
            RestAssured.port = Integer.valueOf(port);
        }


        String basePath = System.getProperty("spring.application.name");
        if(basePath == null){
            basePath = "/";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if(baseHost==null){
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;
		
		this.book = new Book();
		this.book.setId(1L);
		this.book.setBookId("B1212");
		this.book.setName("History of Amazon Valley");
		this.book.setAuthor("Ross Suarez");
		this.book.setCopiesAvailable(2);
		this.book.setTotalCopies(2);
    }
	
	@Test 	
	void saveBook() throws Exception {	
		
		Response response = given()
			.contentType(ContentType.JSON)
			.body(this.book)
		.when()
			.post("/books/saveBook")	
		.then()
		.extract().response();
		
		Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());
		Assertions.assertEquals("B1212", response.jsonPath().getString("bookId"));
		Assertions.assertEquals("History of Amazon Valley", response.jsonPath().getString("name"));
	}
	
	@Test 
	void getBooks() throws Exception {	
		
		Response response = given()
		.when()
			.get("/books")	
		.then()
		.extract().response();
		
		List<Book> books = response.jsonPath().getList(".", Book.class);
		
		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
		Assertions.assertEquals("B1212", books.get(0).getBookId());
		Assertions.assertEquals("History of Amazon Valley", books.get(0).getName());
	}
	
	@Test 
	void getBook() throws Exception {	
        Response response = given()
        		.when()
        			.get("/books/B1212")	
        		.then()
        		.extract().response();
        		
        		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
        		Assertions.assertEquals("B1212", response.jsonPath().getString("bookId"));
        		Assertions.assertEquals("History of Amazon Valley", response.jsonPath().getString("name"));
	        		
	}
	
}
