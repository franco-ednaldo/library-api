package com.example.libraryapi.service;

import com.example.libraryapi.exception.BookNotFound;
import com.example.libraryapi.exception.BusinessException;
import com.example.libraryapi.model.entity.Book;
import com.example.libraryapi.model.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (verifyIsbnDuplicate(book.getIsbn())) {
            throw new BusinessException("Isbn já casdastrado!");
        }

        var savedbook = this.repository.save(book);
        return savedbook;
    }

    @Override
    public Book findBookById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new BookNotFound("Book não encontrado"));
    }

    @Override
    public void update(Book book) {
        if (Objects.nonNull(book) && Objects.nonNull(book.getId())) {
            this.repository.save(book);
        }
    }

    @Override
    public void delete(Book book) {
        if (Objects.nonNull(book) && Objects.nonNull(book.getId())) {
            this.repository.delete(book);
        }
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {
        Example<Book> example = Example.of(filter,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(StringMatcher.CONTAINING)
        );
        return repository.findAll(example, pageRequest);
    }

    @Override
    public Book findBookByIsbn(String isbn) {
        return this.repository.findByIsbn(isbn);
    }

    private boolean verifyIsbnDuplicate(String isbn) {
        return this.repository.existsByIsbn(isbn);
    }
}
