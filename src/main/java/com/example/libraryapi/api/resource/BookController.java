package com.example.libraryapi.api.resource;

import com.example.libraryapi.api.dto.BookDto;
import com.example.libraryapi.api.erros.ApiErros;
import com.example.libraryapi.exception.BookNotFound;
import com.example.libraryapi.exception.BusinessException;
import com.example.libraryapi.model.entity.Book;
import com.example.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@Valid @RequestBody BookDto bookDto) {
        var book = modelMapper.map(bookDto, Book.class);
        var entity = this.bookService.save(book);

        var bookDtoResult = modelMapper.map(entity, BookDto.class);
        return bookDtoResult;

    }

    @GetMapping("{id}")
    public BookDto findById(@PathVariable Integer id) {
        var book = this.bookService.findBookById(id);
        return modelMapper.map(book, BookDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") final Integer id, @RequestBody BookDto bookDto) {
        var book = this.bookService.findBookById(id);
        this.bookService.update(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final Integer id) {
        var book = this.bookService.findBookById(id);
        this.bookService.delete(book);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErros(bindingResult);
    }
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleBusinessException(BusinessException ex) {
        return new ApiErros(ex);
    }

    @ExceptionHandler(BookNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErros handleBusinessException(BookNotFound ex) {
        return new ApiErros(ex);
    }


}
