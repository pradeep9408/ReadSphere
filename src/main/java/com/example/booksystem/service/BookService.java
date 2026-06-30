package com.example.booksystem.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Cacheable("books")
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

    @Cacheable(value = "bookByAuthor", key = "#authorName")
    public List<Book> getBookByAuthor(String authorName) {

        return books.stream()
                .filter(book ->
                        book.getAuthorName()
                                .equalsIgnoreCase(authorName))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "bookByCategory", key = "#category")
    public List<Book> getBookByCategory(String category) {

        return books.stream()
                .filter(book ->
                        book.getCategory()
                                .equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Cacheable("groupBooksByCategory")
    public Map<String, List<Book>> groupBooksByCategory() {

        return books.stream()
                .collect(Collectors.groupingBy(Book::getCategory));
    }

    @Cacheable("groupBooksByAuthor")
    public Map<String, List<Book>> groupBooksByAuthor() {

        return books.stream()
                .collect(Collectors.groupingBy(Book::getAuthorName));
    }

    @CacheEvict(
            value = {
                    "books",
                    "bookById",
                    "bookByAuthor",
                    "bookByCategory"
            },
            allEntries = true
    )
    public boolean deleteBookById(int id) {

        return books.removeIf(book -> book.getId() == id);
    }

    @CacheEvict(
            value = {
                    "books",
                    "bookById",
                    "bookByAuthor",
                    "bookByCategory"
            },
            allEntries = true
    )
    public boolean deleteBookByAuthor(String authorName) {

        return books.removeIf(book -> book.getAuthorName().equalsIgnoreCase(authorName));
    }

    @CacheEvict(
            value = {
                    "books",
                    "bookById",
                    "bookByAuthor",
                    "bookByCategory"
            },
            allEntries = true
    )
    public boolean deleteBookByCategory(String category) {

        return books.removeIf(book -> book.getCategory().equalsIgnoreCase(category));
    }

    public List<Book> getBooksWithPaginationAndSorting(
            int page,
            int size,
            String sortBy,
            String order) {

        Stream<Book> stream = books.stream();

        Comparator<Book> comparator;

        switch (sortBy.toLowerCase()) {

            case "bookname":
                comparator = Comparator.comparing(Book::getBookName);
                break;

            case "authorname":
                comparator = Comparator.comparing(Book::getAuthorName);
                break;

            case "category":
                comparator = Comparator.comparing(Book::getCategory);
                break;

            case "price":
                comparator = Comparator.comparing(Book::getPrice);
                break;

            case "quantity":
                comparator = Comparator.comparing(Book::getQuantity);
                break;

            case "publishedyear":
                comparator = Comparator.comparing(Book::getPublishedYear);
                break;

            default:
                comparator = Comparator.comparing(Book::getId);
        }

        if (order.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        return stream
                .sorted(comparator)
                .skip((long) page * size)
                .limit(size)
                .toList();
    }
}