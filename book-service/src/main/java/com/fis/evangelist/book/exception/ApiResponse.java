package com.fis.evangelist.book.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private final String message;
    private final int status;
}
