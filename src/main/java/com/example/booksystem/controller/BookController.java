package com.example.booksystem.controller;

import com.example.booksystem.dto.ErrorResponse;
import com.example.booksystem.dto.ReportResponse;
import com.example.booksystem.enums.Role;
import com.example.booksystem.model.Book;
import com.example.booksystem.service.BookService;
import com.example.booksystem.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "Book Controller", description = "APIs for managing books")
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

    @Operation(summary = "Get book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("id")
    public ResponseEntity<?> getBookById(
            @RequestParam
            @Min(value=1, message = "Book id must be greater than 0")
            int id) {

        Book book = bookService.getBookById(id);

        if(book==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book not found"));
        }
        log.info("Loaded book by id : {} ", id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Get books by author")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
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

    @Operation(summary = "Get books by category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
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

    @Operation(summary = "Group books by category")
    @GetMapping("group/category")
    public ResponseEntity<Map<String, List<Book>>> groupBooksByCategory() {

        Map<String, List<Book>> groupedBooks = bookService.groupBooksByCategory();

        if(groupedBooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Loaded books grouped by category", groupedBooks);
        return ResponseEntity.ok(groupedBooks);
    }

    @Operation(summary = "Group books by author")
    @GetMapping("group/author")
    public ResponseEntity<Map<String, List<Book>>> groupBooksByAuthor() {

        Map<String, List<Book>> groupedAuthorBooks = bookService.groupBooksByAuthor();

        if(groupedAuthorBooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Loaded books grouped by author : {} ", groupedAuthorBooks);
        return ResponseEntity.ok(groupedAuthorBooks);
    }

    @Operation(summary = "Generate report file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Report generated successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to generate report")
    })
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

    @Operation(summary = "Get report in JSON format")
    @GetMapping("report/json")
    public ResponseEntity<ReportResponse> getReport() {

        return ResponseEntity.ok(
                reportService.getReport());
    }

    @Operation(summary = "Delete book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Only admin can delete books"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("id")
    public ResponseEntity<String> deleteBookById(
            @RequestHeader("role") String role,
            @RequestParam int id) {

        Role userRole = Role.valueOf(role.toUpperCase());

        if (userRole != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can delete books");
        }

        boolean deleted = bookService.deleteBookById(id);

        if (deleted) {
            return ResponseEntity.ok("Book deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found");
    }

    @Operation(summary = "Delete books by author")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Only admin can delete books"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @DeleteMapping("author")
    public ResponseEntity<String> deleteBookByAuthor(
            @RequestHeader("role") String role,
            @RequestParam String authorName) {

        Role userRole = Role.valueOf(role.toUpperCase());

        if (userRole != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can delete books");
        }

        boolean deleted = bookService.deleteBookByAuthor(authorName);

        if (deleted) {
            return ResponseEntity.ok("Books deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Author not found");
    }

    @Operation(summary = "Delete books by category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Only admin can delete books"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("category")
    public ResponseEntity<String> deleteBookByCategory(
            @RequestHeader("role") String role,
            @RequestParam String category) {

        Role userRole = Role.valueOf(role.toUpperCase());

        if (userRole != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can delete books");
        }

        boolean deleted = bookService.deleteBookByCategory(category);

        if (deleted) {
            return ResponseEntity.ok("Books deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Category not found");
    }

    @Operation(summary = "Get books with pagination and sorting")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/pagination")
    public ResponseEntity<List<Book>> getBooksWithPagination(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String order) {

        List<Book> books =
                bookService.getBooksWithPaginationAndSorting(
                        page,
                        size,
                        sortBy,
                        order);

        return ResponseEntity.ok(books);
    }
}
