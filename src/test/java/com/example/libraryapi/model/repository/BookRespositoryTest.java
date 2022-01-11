package com.example.libraryapi.model.repository;

import com.example.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRespositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve salvar um book")
    public void createBook() {
        var book = this.getBook("Fulano", "As aventuras de Fulano", "1235");
        book = this.repository.save(book);

        assertThat(book).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um book")
    public void deleteBook() {
        var book = this.getBook("Fulano", "As aventuras de fulano", "1235");
        this.persist(book);
        this.repository.delete(book);

        var bookDeleted = this.repository.findById(book.getId());
        assertThat(bookDeleted).isEqualTo(Optional.empty());

    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro com isbn informado")
    public void returnTrueWhenIsbnExists() {
        String isbn = "123";
        var book = getBook("Fulano", "As aventuras de fulano", isbn);
        this.persist(book);

        boolean exists = this.repository.existsByIsbn(isbn);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir um livro com isbn informado")
    public void returnFalseWhenDoesntIsbn() {
        String isbn = "123";
        boolean exists = this.repository.existsByIsbn(isbn);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar um livro consultado pelo id")
    public void returnBookById(){
        var book = getBook("Faulano", "Aventuras de Fulano", "123456");
        this.persist(book);
        var bookById = this.repository.findById(book.getId());
        assertThat(bookById.isPresent()).isTrue();
    }

    private Book getBook(String author, String title, String isbn) {
        return Book.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .build();
    }

    public void persist(Book book) {
        book = this.testEntityManager.persist(book);
    }
}
