package com.example.libraryapi.service;


import com.example.libraryapi.model.entity.Book;

public interface BookService {

    Book save(Book book);

    Book findBookById(Integer id);

    void update(Integer id, Book book);

    void delete(Book id);
}
