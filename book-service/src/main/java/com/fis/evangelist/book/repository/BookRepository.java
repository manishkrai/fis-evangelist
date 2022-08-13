package com.fis.evangelist.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fis.evangelist.book.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Book findByBookId(String bookId);
}
