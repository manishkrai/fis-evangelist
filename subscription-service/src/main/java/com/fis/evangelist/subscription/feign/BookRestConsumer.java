package com.fis.evangelist.subscription.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fis.evangelist.subscription.VO.Book;

@FeignClient(name = "book-service", path="/books")
public interface BookRestConsumer {
	
	@PostMapping("/saveBook")
	ResponseEntity<Book> saveBook(Book book);
	
	@GetMapping("")
	List<Book> getBooks();
	
	@GetMapping("/getBook/{bookId}")
	Book getBook(@PathVariable(value = "bookId")String bookId);

}
