package com.fis.evangelist.book.validator;

import org.apache.commons.lang.StringUtils;

import com.fis.evangelist.model.BookRequest;

public class BookRequestValidator {

    public static void validate(BookRequest bookRequest) {
        if (bookRequest == null) {
            throw new IllegalArgumentException("BookRequest cannot be null");
        }

        validateRequiredField("bookId", bookRequest.getBookId());
        validateRequiredField("name", bookRequest.getName());
        validateRequiredField("author", bookRequest.getAuthor());
    }

    private static void validateRequiredField(String fieldName, String fieldValue) {
        if (StringUtils.isEmpty(fieldValue)) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }
}
