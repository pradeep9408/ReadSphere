package com.example.booksystem.controller;

import com.example.booksystem.model.Book;
import com.example.booksystem.service.BookService;
import com.example.booksystem.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@Validated
@RequestMapping("books")
public class BookController {

    private final BookService bookService;
    private final ReportService reportService;

    public BookController(BookService bookService, ReportService reportService) {
        this.bookService = bookService;
        this.reportService = reportService;
    }

    @GetMapping
    public List<Book> getBooks() {

        return bookService.getBooks();
    }

    @GetMapping("id")
    public ResponseEntity<Book> getBookById(
            @RequestParam
            @Min(value=1, message = "Book id must be greater than 0")
            int id) {

        Book book = bookService.getBookById(id);

        if(book==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("Loaded book by id : {} ", id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("author")
    public ResponseEntity<List<Book>> getBookByAuthor(
            @RequestParam
            @NotBlank(message = "Author name cannot be empty")
            String authorName) {

        List<Book> books = bookService.getBookByAuthor(authorName);

        if(books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(books);
        }
        log.info("Loaded books by author", authorName);
        return ResponseEntity.ok(books);
    }

    @GetMapping("category")
    public ResponseEntity<List<Book>> getBookByCategory(
            @RequestParam
            @NotBlank(message = "Category cannot be empty")
            String category) {

            List<Book> books = bookService.getBookByCategory(category);

            if(books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(books);
            }
            log.info("Loaded books by category", category);
            return ResponseEntity.ok(books);
    }
    @GetMapping("group/category")
    public ResponseEntity<Map<String, List<Book>>> groupBooksByCategory() {

        Map<String, List<Book>> groupedBooks = bookService.groupBooksByCategory();

        if(groupedBooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Loaded books grouped by category", groupedBooks);
        return ResponseEntity.ok(groupedBooks);
    }
    @GetMapping("group/author")
    public ResponseEntity<Map<String, List<Book>>> groupBooksByAuthor() {

        Map<String, List<Book>> groupedAuthorBooks = bookService.groupBooksByAuthor();

        if(groupedAuthorBooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Loaded books grouped by author : {} ", groupedAuthorBooks);
        return ResponseEntity.ok(groupedAuthorBooks);
    }
    @GetMapping("report")
    public ResponseEntity<String> generateReport() {
        try {
            reportService.generateReport();

            return ResponseEntity.status(HttpStatus.OK).body("Report generated successfully");
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate report");
        }
    }
}
