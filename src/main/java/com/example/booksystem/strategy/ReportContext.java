package com.example.booksystem.strategy;

import com.example.booksystem.model.Book;

import java.util.List;

public class ReportContext {

    private ReportStrategy reportStrategy;

    public ReportContext(ReportStrategy reportStrategy) {
        this.reportStrategy = reportStrategy;
    }

    public String generate(List<Book> books) {
        return reportStrategy.generateReport(books);
    }
}
