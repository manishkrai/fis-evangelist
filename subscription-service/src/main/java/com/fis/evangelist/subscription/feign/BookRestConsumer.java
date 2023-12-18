package com.fis.evangelist.subscription.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fis.evangelist.subscription.model.BookRequest;
import com.fis.evangelist.subscription.model.BookResponse;

@FeignClient(name = "book-service", url = "${book-service.url}")
public interface BookRestConsumer {
		
	@PostMapping("/books/saveBook")
	ResponseEntity<BookResponse> saveBook(BookRequest bookRequest);
	
	@GetMapping("/books")
	List<BookResponse> getBooks();
	
	@GetMapping("/books/{bookId}")
	BookResponse getBook(@PathVariable(value = "bookId")String bookId);

}
