package com.example.booksystem.strategy;

import com.example.booksystem.model.Book;

import java.util.List;

public interface ReportStrategy {
    String generateReport(List<Book> books);
}
