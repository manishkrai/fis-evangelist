package com.fis.evangelist.subscription.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BookRequest {

    private String bookId;
    private String name;
    private String author;
    private int copiesAvailable;
    private int totalCopies;
}
