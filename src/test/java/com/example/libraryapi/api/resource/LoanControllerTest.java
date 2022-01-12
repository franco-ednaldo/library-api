package com.example.libraryapi.api.resource;

import com.example.libraryapi.api.dto.LoanDto;
import com.example.libraryapi.model.entity.Book;
import com.example.libraryapi.model.entity.Loan;
import com.example.libraryapi.service.BookService;
import com.example.libraryapi.service.LoanService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {
    static final String LOAN_API = "/api/loans";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @MockBean
    LoanService loanService;

    @Test
    @DisplayName("Deve salvar uma reserva de um book")
    public void createLoanTest() throws Exception {
        var isbn = "123";
        final Integer bookId = 1;
        final Integer loanId = 1;
        var loanDto = this.createLoanDto(isbn, "Fulano");
        var json = new ObjectMapper().writeValueAsString(loanDto);
        var bookReturned = createBookEntity(bookId, "Fulano", "As aventuras de Fulano", isbn);
        var loanReturned = createLoanEntity(loanId, "Fulano", bookReturned);

        BDDMockito.given(this.bookService.findBookByIsbn(isbn)).willReturn(bookReturned);
        BDDMockito.given(this.loanService.save(Mockito.any(Loan.class))).willReturn(loanReturned);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    private Loan createLoanEntity(Integer loanId, String customer, Book book) {
        return Loan.builder()
                .id(loanId)
                .book(book)
                .customer(customer)
                .build();
    }

    private Book createBookEntity(Integer bookId, String author, String title, String isbn) {
        return Book.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .build();
    }

    private LoanDto createLoanDto(final String isbn, final String customer) {
        return LoanDto.builder()
                .customer(customer)
                .isbn(isbn)
                .build();
    }
}
