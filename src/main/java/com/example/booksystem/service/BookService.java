package com.example.booksystem.service;

import com.example.booksystem.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.example.booksystem.constants.AppConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BookService {

    public List<Book> getBooks() {

        try {

            ClassPathResource resource = new ClassPathResource(AppConstants.BOOKS_CSV_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            br.readLine();

            List<Book> books = br.lines()
                            .map(line -> line.split(","))
                            .map(data -> Book.builder()
                                            .id(Integer.parseInt(data[0]))
                                            .bookName(data[1])
                                            .authorName(data[2])
                                            .category(data[3])
                                            .publisher(data[4])
                                            .price(Double.parseDouble(data[5]))
                                            .quantity(Integer.parseInt(data[6]))
                                            .publishedYear(Integer.parseInt(data[7]))
                                            .isbn(data[8])
                                            .language(data[9])
                                    .build())
                            .collect(Collectors.toList());

            log.info("Loaded {} books from CSV", books.size());
            return books;
        } catch (Exception e) {
            log.error("Error while reading CSV file", e);
            return List.of();
        }
    }

    public Book getBookById(int id) {

        return getBooks().stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Book> getBookByAuthor(String authorName) {

        return getBooks().stream()
                .filter(book -> book.getAuthorName().equalsIgnoreCase(authorName))
                .collect(Collectors.toList());
    }

    public List<Book> getBookByCategory(String category) {

        return getBooks().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }


    public Map<String, List<Book>> groupBooksByCategory() {

        return getBooks()
                .stream()
                .collect(Collectors.groupingBy(Book::getCategory));
    }
    public Map<String, List<Book>> groupBooksByAuthor() {

        return getBooks()
                .stream()
                .collect(Collectors.groupingBy(Book::getAuthorName));
    }
}