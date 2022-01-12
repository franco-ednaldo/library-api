package com.example.libraryapi.api.resource;

import com.example.libraryapi.api.dto.LoanDto;
import com.example.libraryapi.model.entity.Loan;
import com.example.libraryapi.service.BookService;
import com.example.libraryapi.service.LoanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@RequestBody @Valid LoanDto loanDto) {
        var book = this.bookService.findBookById(loanDto.getId());
        var loan = Loan.builder()
                .book(book)
                .customer(loanDto.getCustomer())
                .loanDate(LocalDate.now())
                .build();
        var loanSaved = this.loanService.save(loan);
        return loanSaved.getId();
    }
}
