package com.fis.evangelist.model;

import lombok.Data;

@Data
public class BookResponse {

    private Long id;
    private String bookId;
    private String name;
    private String author;
    private int copiesAvailable;
    private int totalCopies;
}
