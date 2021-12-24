package com.example.libraryapi;

import com.example.libraryapi.model.entity.Book;
import com.example.libraryapi.service.BookService;
import com.example.libraryapi.service.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    private BookService service;

    public void setUp() {
        this.service = new BookServiceImpl();
    }


    @Test
    @DisplayName("Deve criar o livro com sucesso")
    public void create() {
        var book = Book.builder()
                .author("Fulano")
                .title("As Aventuras BlaBla")
                .isbn("123555")
                .build();
        var salvedBook = this.service.save(book);

        assertThat(salvedBook.getId()).isGreaterThan(0);
        assertThat(salvedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(salvedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(salvedBook.getIsbn()).isEqualTo(book.getIsbn());
    }
}
