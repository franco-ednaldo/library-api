package com.example.libraryapi.api.resource;

import com.example.libraryapi.api.dto.BookDto;
import com.example.libraryapi.exception.BookNotFound;
import com.example.libraryapi.exception.BusinessException;
import com.example.libraryapi.model.entity.Book;
import com.example.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {
    static String BOOK_API = "/api/books";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {
        var book = BookDto.builder()
                .title("As aventuras do rei Arhtur")
                .author("Arthur")
                .isbn("122555")
                .build();
        var savedBook = Book.builder()
                .id(1)
                .title("As aventuras do rei Arhtur")
                .author("Arthur")
                .isbn("122555")
                .build();
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(book);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(book.getTitle()))
                .andExpect(jsonPath("author").value(book.getAuthor()))
                .andExpect(jsonPath("isbn").value(book.getIsbn()));
    }

    @Test
    @DisplayName("Deve lançar um erro de validação")
    public void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("erros", hasSize(3)));
    }

    @Test
    @DisplayName("Deve retornar erro em caso de isbn duplicado")
    public void createBookWithIsbnDuplicate() throws Exception {
        String json = new ObjectMapper().writeValueAsString(BookDto.builder()
                .isbn("123333")
                .title("As Aventuras de blabla")
                .author("Fulano")
                .build());

        String messageError = "Isbn já casdastrado!";
        BDDMockito.given(this.bookService.save(Mockito.any())).willThrow(new BusinessException(messageError));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("erros", hasSize(1)))
                .andExpect(jsonPath("erros[0]").value(messageError));

    }

    @Test
    @DisplayName("Deve atualizar um book")
    public void update() throws Exception {
        final Integer bookId = 1;
        var book = Book.builder()
                .id(bookId)
                .author("Fulano")
                .title("Aventuras de Fulano")
                .isbn("123")
                .build();

        BDDMockito.given(this.bookService.findBookById(anyInt())).willReturn(book);
        String json = new ObjectMapper().writeValueAsString(BookDto.builder()
                .isbn("123")
                .title("Aventuras de Fulano")
                .author("Fulano")
                .build());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(BOOK_API.concat("/").concat(bookId.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar exception quando o livro não for encontrado um book")
    public void updateWihtBookNotFound() throws Exception {
        final Integer bookId = 1;

        BDDMockito.given(this.bookService.findBookById(anyInt()))
                .willThrow(new BookNotFound("Book não encontrado"));

        String json = new ObjectMapper().writeValueAsString(BookDto.builder()
                .isbn("123")
                .title("Aventuras de Fulano")
                .author("Fulano")
                .build());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(BOOK_API.concat("/").concat(bookId.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBook() throws Exception {
        final Integer bookId = 1;
        BDDMockito.given(this.bookService.findBookById(bookId)).willReturn(
                Book.builder()
                        .id(bookId)
                        .build()
        );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1));
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve lança uma exception quando livro não encontrado")
    public void deleteBookNotFound() throws Exception {
        final Integer bookId = 1;
        BDDMockito.given(this.bookService.findBookById(bookId))
                .willThrow(new BookNotFound("Book não encontrado"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar uma lista de books paginada")
    public void findBooks() throws Exception {
        var book = Book.builder()
                .id(1)
                .isbn("123")
                .title("As aventuras de Fulano")
                .author("Fulano da Silva")
                .build();
        BDDMockito.given(this.bookService.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1));
        String queryString = String.format("?title=%s&author=%s&page=0&size=100", book.getTitle(), book.getAuthor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    @Test
    @DisplayName("Deve retornar um livro pelo id")
    public void findBookById() throws Exception {
        var id = 1;
        var book = Book.builder()
                .id(id)
                .author("Fulano")
                .title("Aventuras de fulano")
                .isbn("123")
                .build();
        BDDMockito.given(this.bookService.findBookById(id)).willReturn(book);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders
                        .get(BOOK_API.concat("/" + id))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("author").value("Fulano"))
                .andExpect(jsonPath("title").value("Aventuras de fulano"))
                .andExpect(jsonPath("isbn").value("123"));
    }

    @Test
    @DisplayName("Deve retornar o código de status 404 quando não encontra o livro")
    public void findByIdNotFound() throws Exception {
        var id = 1;
        var messageError = "Book não encontrado";

        BDDMockito.given(this.bookService.findBookById(id)).willThrow(new BookNotFound(messageError));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("erros", hasSize(1)))
                .andExpect(jsonPath("erros[0]").value(messageError));
    }


}
