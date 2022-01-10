package com.example.libraryapi.exception;

import java.util.function.Supplier;

public class BookNotFound extends BusinessException {
    public BookNotFound() {
        super();
    }

    public BookNotFound(String message) {
        super(message);
    }
}
