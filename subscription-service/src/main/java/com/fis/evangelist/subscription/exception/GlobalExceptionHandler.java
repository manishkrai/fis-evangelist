package com.fis.evangelist.subscription.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateBookException.class)
    public ResponseEntity<Object> handleDuplicateBookException(DuplicateBookException ex) {
        return new ResponseEntity<>(new ApiResponse("Error saving book: Duplicate book found.", HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse("Error fetching book: Book is not available.", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
}