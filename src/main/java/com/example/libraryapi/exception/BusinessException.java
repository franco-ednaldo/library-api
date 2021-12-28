package com.example.libraryapi.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(){
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}
