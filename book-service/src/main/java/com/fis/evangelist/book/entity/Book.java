package com.fis.evangelist.book.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="BOOK")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="BOOK_ID")
	private String bookId;
	
	@Column(name="BOOK_NAME")
	private String name;
	
	@Column(name="AUTHOR")
	private String author;
	
	@Column(name="AVAILABLE_COPIES")
	private int copiesAvailable;
	
	@Column(name="TOTAL_COPIES")
	private int totalCopies;

}
