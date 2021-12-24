package com.example.libraryapi.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Integer id;

    private String title;

    private String author;

    private String isbn;
}
