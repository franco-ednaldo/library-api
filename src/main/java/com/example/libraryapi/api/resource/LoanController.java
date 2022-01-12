package com.example.libraryapi.api.resource;

import com.example.libraryapi.api.dto.LoanDto;
import com.example.libraryapi.model.entity.Loan;
import com.example.libraryapi.service.LoanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanDto create(@RequestBody @Valid LoanDto loanDto) {
        var loan = modelMapper.map(loanDto, Loan.class);
        var loanSaved = this.loanService.save(loan);
        return Optional.of(loanSaved)
                .map(l -> modelMapper.map(l, LoanDto.class))
                .get();
    }
}
