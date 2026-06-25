package com.example.booksystem.service;

import com.example.booksystem.constants.AppConstants;
import com.example.booksystem.factory.BookFactory;
import com.example.booksystem.model.Book;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookService {

    private List<Book> books;

    @PostConstruct
    public void loadBooks() {

        try {

            ClassPathResource resource =
                    new ClassPathResource(AppConstants.BOOKS_CSV_FILE);

            try (BufferedReader br =
                         new BufferedReader(
                                 new InputStreamReader(resource.getInputStream()))) {

                br.readLine();

                books = br.lines()
                        .map(line -> line.split(","))
                        .map(data -> BookFactory.createBook(
                                Integer.parseInt(data[0]),
                                data[1],
                                data[2],
                                data[3],
                                data[4],
                                Double.parseDouble(data[5]),
                                Integer.parseInt(data[6]),
                                Integer.parseInt(data[7]),
                                data[8],
                                data[9]
                        ))
                        .collect(Collectors.toCollection(ArrayList::new));

                log.info("Loaded {} books from CSV", books.size());
            }

        } catch (Exception e) {

            log.error("Error while reading CSV file", e);

            books = List.of();
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getBookById(int id) {

        Book book = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);

        if (book == null) {
            log.warn("Book not found with id {}", id);
        }

        return book;
    }

    public List<Book> getBookByAuthor(String authorName) {

        return books.stream()
                .filter(book ->
                        book.getAuthorName()
                                .equalsIgnoreCase(authorName))
                .collect(Collectors.toList());
    }

    public List<Book> getBookByCategory(String category) {

        return books.stream()
                .filter(book ->
                        book.getCategory()
                                .equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public Map<String, List<Book>> groupBooksByCategory() {

        return books.stream()
                .collect(Collectors.groupingBy(Book::getCategory));
    }

    public Map<String, List<Book>> groupBooksByAuthor() {

        return books.stream()
                .collect(Collectors.groupingBy(Book::getAuthorName));
    }

    public boolean deleteBookById(int id) {

        return books.removeIf(book -> book.getId() == id);
    }

    public boolean deleteBookByAuthor(String authorName) {

        return books.removeIf(book -> book.getAuthorName() == authorName);
    }

    public boolean deleteBookByCategory(String category) {

        return books.removeIf(book -> book.getCategory() == category);
    }
}