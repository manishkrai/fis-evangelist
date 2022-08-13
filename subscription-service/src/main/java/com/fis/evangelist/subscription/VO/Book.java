package com.fis.evangelist.subscription.VO;

import lombok.Data;

@Data
public class Book {
	private Long id;
	private String bookId;
	private String name;
	private String author;
	private Long copiesAvailable;
	private Long totalCopies;
}