package com.example.libraryapi.service;


import com.example.libraryapi.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Book save(Book book);

    Book findBookById(Integer id);

    void update(Book book);

    void delete(Book id);

    Page<Book> find(Book filter, Pageable pageRequest);
}
