package com.example.booksystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private int id;
    private String bookName;
    private String authorName;
    private String category;
    private String publisher;
    private double price;
    private int quantity;
    private int publishedYear;
    private String isbn;
    private String language;
}