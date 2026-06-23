package com.example.booksystem.strategy;

import com.example.booksystem.model.Book;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryReportStrategy implements ReportStrategy {

    @Override
    public String generateReport(List<Book> books) {
        Map<String, Long> categoryCount = books.stream()
                .collect(Collectors.groupingBy(Book :: getCategory, Collectors.counting()));
        return categoryCount.toString();
    }

}
