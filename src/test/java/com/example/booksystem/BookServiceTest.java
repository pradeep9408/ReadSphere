package com.example.booksystem;

import com.example.booksystem.model.Book;
import com.example.booksystem.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {

        bookService = new BookService();
        bookService.loadBooks();
    }

    @Test
    void testGetById() {

        Book book = bookService.getBookById(2);

        assertNotNull(book);
        assertEquals(2, book.getId());
    }

    @Test
    void testGetByCategory() {

        List<Book> books =
                bookService.getBookByCategory("Programming");

        assertFalse(books.isEmpty());

        books.forEach(book ->
                assertEquals("Programming",
                        book.getCategory()));
    }

    @Test
    void testGetByAuthor() {

        List<Book> books =
                bookService.getBookByAuthor("Craig Walls");

        assertFalse(books.isEmpty());

        books.forEach(book ->
                assertEquals("Craig Walls",
                        book.getAuthorName()));
    }
}