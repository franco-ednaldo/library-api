package com.example.libraryapi.service;

import com.example.libraryapi.model.entity.Book;
import com.example.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        var savedbook = this.repository.save(book);
        return savedbook;
    }
}
