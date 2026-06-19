package com.library.book_service;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import models.Book;
import repositories.BookRepository;

@Configuration
public class DataInitializer {
	
	@Bean
    public CommandLineRunner seedBooks(BookRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Book b1 = new Book();
                b1.setIsbn("978-0134685991");
                b1.setTitle("Effective Java");
                b1.setAuthor("Joshua Bloch");
                b1.setGenre("Programming");
                b1.setDescription("Best practices for the Java platform");
                b1.setPublishedYear(2018);
                b1.setPublisher("Addison-Wesley");
                b1.setTotalCopies(5);
                b1.setAvailableCopies(5);

                Book b2 = new Book();
                b2.setIsbn("978-0596007126");
                b2.setTitle("Head First Design Patterns");
                b2.setAuthor("Eric Freeman");
                b2.setGenre("Programming");
                b2.setDescription("A brain-friendly guide to design patterns");
                b2.setPublishedYear(2004);
                b2.setPublisher("O'Reilly Media");
                b2.setTotalCopies(3);
                b2.setAvailableCopies(3);

                repository.save(b1);
                repository.save(b2);
            }
        };
    }
}
