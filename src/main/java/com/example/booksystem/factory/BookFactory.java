package com.example.booksystem.factory;

import com.example.booksystem.model.Book;

public class BookFactory {
    public static Book createBook(int id,
                                  String bookName,
                                  String authorName,
                                  String category,
                                  String publisher,
                                  double price,
                                  int quantity,
                                  int publishedYear,
                                  String isbn,
                                  String language) {
        return Book.builder()
                .id(id)
                .bookName(bookName)
                .authorName(authorName)
                .category(category)
                .publisher(publisher)
                .publishedYear(publishedYear)
                .price(price)
                .quantity(quantity)
                .publishedYear(publishedYear)
                .isbn(isbn)
                .language(language)
                .build();
    }
}
