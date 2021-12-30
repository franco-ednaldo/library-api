package com.example.libraryapi.service;

import com.example.libraryapi.exception.BusinessException;
import com.example.libraryapi.model.entity.Book;
import com.example.libraryapi.model.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    private BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }


    @Test
    @DisplayName("Deve criar o livro com sucesso")
    public void create() {
        var book = this.createComponente(0, "Fulano",
                "As Aventuras BlaBla", "123555");

        var bookResult = this.createComponente(1, "Fulano",
                "As Aventuras BlaBla", "123555");

        Mockito.when(repository.save(book)).thenReturn(bookResult);
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        var salvedBook = this.service.save(book);

        assertThat(salvedBook.getId()).isGreaterThan(0);
        assertThat(salvedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(salvedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(salvedBook.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve lançar uma exception dizendo que já existe isbn cadastrado")
    public void createBookWithErrorDuplicateIsbn() {
        var book = this.createComponente(0, "Fulano",
                "As aventura de fulano", "123456");

        var bookResult = this.createComponente(0, "Fulano",
                "As aventura de fulano", "123456");

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        Mockito.when(repository.save(book)).thenReturn(bookResult);

        Throwable exeption = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exeption)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já casdastrado!");

        Mockito.verify(repository, Mockito.never()).save(book);

    }


    private Book createComponente(Integer id, String author, String title, String isbn) {
        return Book.builder()
                .id(id)
                .author(author)
                .title(title)
                .isbn(isbn)
                .build();
    }
}
