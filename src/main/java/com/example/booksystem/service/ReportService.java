package com.example.booksystem.service;

import com.example.booksystem.model.Book;
import com.example.booksystem.strategy.AuthorReportStrategy;
import com.example.booksystem.strategy.CategoryReportStrategy;
import com.example.booksystem.strategy.ReportContext;
import com.example.booksystem.strategy.ReportStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.booksystem.constants.AppConstants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ReportService {

    private final BookService bookService;

    public ReportService(BookService bookService) {
        this.bookService = bookService;
    }

    public void generateReport() {

        try {

            List<Book> books = bookService.getBooks();

            ReportContext context = new ReportContext(new AuthorReportStrategy());
            String report = context.generate(books);
            log.info("Generated Author Report : {} ", report);

            ReportContext context1 = new ReportContext(new CategoryReportStrategy());
            String category_report = context.generate(books);
            log.info("Generated Category Report : {} ", category_report);

            double averagePrice =
                    books.stream()
                            .mapToDouble(Book::getPrice)
                            .average()
                            .orElse(0);

            double highestPrice =
                    books.stream()
                            .mapToDouble(Book::getPrice)
                            .max()
                            .orElse(0);

            double lowestPrice =
                    books.stream()
                            .mapToDouble(Book::getPrice)
                            .min()
                            .orElse(0);

            int totalQuantity =
                    books.stream()
                            .mapToInt(Book::getQuantity)
                            .sum();

            double totalCost =
                    books.stream()
                            .mapToDouble(book ->
                                    book.getPrice() *
                                            book.getQuantity())
                            .sum();

            String highestBook =
                    books.stream()
                            .max(Comparator.comparing(Book::getPrice))
                            .map(Book::getBookName)
                            .orElse("N/A");

            String lowestBook =
                    books.stream()
                            .min(Comparator.comparing(Book::getPrice))
                            .map(Book::getBookName)
                            .orElse("N/A");

            BufferedWriter bw =
                    new BufferedWriter(
                            new FileWriter(AppConstants.REPORT_FILE));

            bw.write("========== BOOK REPORT ==========");
            bw.newLine();
            bw.write("Total Books : " + books.size());
            bw.newLine();
            bw.write("Average Price : " + averagePrice);
            bw.newLine();
            bw.write("Highest Price : " + highestPrice);
            bw.newLine();
            bw.write("Highest Price Book : " + highestBook);
            bw.newLine();
            bw.write("Lowest Price : " + lowestPrice);
            bw.newLine();
            bw.write("Lowest Price Book : " + lowestBook);
            bw.newLine();
            bw.write("Total Quantity : " + totalQuantity);
            bw.newLine();
            bw.write("Total Cost Of All Books : " + totalCost);
            bw.newLine();

            bw.close();

            log.info("Report Generated Successfully");

        } catch (Exception e) {

            log.error("Error while generating report", e);
        }
    }
}