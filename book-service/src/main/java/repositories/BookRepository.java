package repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import models.Book;

public interface BookRepository extends MongoRepository<Book, String> {
	List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByGenreIgnoreCase(String genre);

    Optional<Book> findByIsbn(String isbn);
}
