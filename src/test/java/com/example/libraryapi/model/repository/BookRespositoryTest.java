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

    private Book getBook(String author, String title, String isbn) {
        return Book.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .build();
    }

    public void persist(Book book) {
        this.testEntityManager.persist(book);
    }
}
