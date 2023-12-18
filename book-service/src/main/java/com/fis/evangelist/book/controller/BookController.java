package com.fis.evangelist.book.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fis.evangelist.book.exception.BookNotFoundException;
import com.fis.evangelist.book.exception.DuplicateBookException;
import com.fis.evangelist.book.service.BookService;
import com.fis.evangelist.model.BookRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
	
	private final BookService bookService;
	
	@Operation(summary = "Save a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book saved successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict: Duplicate book found")
    })
	@PostMapping("/save")
    public ResponseEntity<Object> saveBook(@RequestBody BookRequest bookRequest) {
        log.info("Inside saveBook method of BookController");
        HttpStatus status = HttpStatus.CREATED;
        Object response = null;

        try {
            response = bookService.saveBook(bookRequest);
        } catch (DuplicateBookException e) {
            status = HttpStatus.CONFLICT;
            response = e.getMessage();
        }

        return new ResponseEntity<>(response, status);
    }

	@Operation(summary = "Get book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found: Book not found")
    })
    @GetMapping("/{bookId}")
    public ResponseEntity<Object> getBook(@PathVariable("bookId") String bookId) {
        log.info("Inside getBook method of BookController");
        try {
            return ResponseEntity.ok(bookService.getBook(bookId));
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

	@Operation(summary = "Get all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("")
    public ResponseEntity<Object> getBooks() {
        log.info("Inside getAllBooks method of BookController");
        try {
            return ResponseEntity.ok(bookService.getAllBooks());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
