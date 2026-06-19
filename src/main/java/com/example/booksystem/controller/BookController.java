package com.example.booksystem.controller;

import com.example.booksystem.model.Book;
import com.example.booksystem.service.BookService;
import com.example.booksystem.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
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
    public Book getBookById(@RequestParam int id) {

        return bookService.getBookById(id);
    }

    @GetMapping("author")
    public List<Book> getBookByAuthor(@RequestParam String authorName) {
        return bookService.getBookByAuthor(authorName);
    }

    @GetMapping("category")
    public List<Book> getBookByCategory(@RequestParam String category) {
        return bookService.getBookByCategory(category);
    }
    @GetMapping("group/category")
    public Map<String, List<Book>> groupBooksByCategory() {
        return bookService.groupBooksByCategory();
    }
    @GetMapping("group/author")
    public Map<String, List<Book>> groupBooksByAuthor() {
        return bookService.groupBooksByAuthor();
    }
    @GetMapping("report")
    public String generateReport() {
        reportService.generateReport();
        return "Report generated successfully";
    }
}
