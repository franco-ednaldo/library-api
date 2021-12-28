package com.example.libraryapi.api.erros;

import com.example.libraryapi.exception.BusinessException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErros {
    private List<String> erros;

    public ApiErros(BindingResult bindingResult) {
        this.erros = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.erros.add(error.getDefaultMessage()));
    }

    public ApiErros(BusinessException businessException) {
        this.erros = Arrays.asList(businessException.getMessage());
    }

    public List<String> getErros() {
        return erros;
    }
}
