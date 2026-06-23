package com.example.booksystem.strategy;

import com.example.booksystem.model.Book;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorReportStrategy implements ReportStrategy {

    @Override
    public String generateReport(List<Book> books) {
        Map<String, Long> authorCount = books.stream()
                .collect(Collectors.groupingBy(Book :: getAuthorName, Collectors.counting()));
        return authorCount.toString();
    }
}
