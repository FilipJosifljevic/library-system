package services;

import java.util.List;

import org.springframework.stereotype.Service;

import dtos.BookAvailabilityResponse;
import dtos.BookRequest;
import dtos.BookResponse;
import exceptions.BookNotAvailableException;
import exceptions.BookNotFoundException;
import models.Book;
import repositories.BookRepository;

@Service
public class BookService {
	
	private final BookRepository repository;

	public BookService(BookRepository repository) {
		super();
		this.repository = repository;
	}
	
	public BookResponse create(BookRequest request) {
		Book book = new Book();
		book.setIsbn(request.isbn());
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        book.setDescription(request.description());
        book.setPublishedYear(request.publishedYear());
        book.setPublisher(request.publisher());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.totalCopies());

        repository.save(book);
        return map(book);
	}
	
	public BookResponse getById(String id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return map(book);
    }

    public List<BookResponse> getAll() {
        return repository.findAll().stream()
                .map(this::map)
                .toList();
    }
    
    public List<BookResponse> search(String title, String author, String genre) {
        List<Book> results;

        if (title != null) {
            results = repository.findByTitleContainingIgnoreCase(title);
        } else if (author != null) {
            results = repository.findByAuthorContainingIgnoreCase(author);
        } else if (genre != null) {
            results = repository.findByGenreIgnoreCase(genre);
        } else {
            results = repository.findAll();
        }

        return results.stream()
                .map(this::map)
                .toList();
    }

    public BookResponse update(String id, BookRequest request) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        int loanedCopies = book.getTotalCopies() - book.getAvailableCopies();

        book.setIsbn(request.isbn());
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        book.setDescription(request.description());
        book.setPublishedYear(request.publishedYear());
        book.setPublisher(request.publisher());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(Math.max(0, request.totalCopies() - loanedCopies));

        repository.save(book);
        return map(book);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public BookAvailabilityResponse checkAvailability(String id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return new BookAvailabilityResponse(
                book.getId(),
                book.getTitle(),
                book.getAvailableCopies(),
                book.getAvailableCopies() > 0
        );
    }

    public void decreaseAvailableCopies(String id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException(id);
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        repository.save(book);
    }

    public void increaseAvailableCopies(String id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (book.getAvailableCopies() < book.getTotalCopies()) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            repository.save(book);
        }
    }

    private BookResponse map(Book book) {
        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getDescription(),
                book.getPublishedYear(),
                book.getPublisher(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }
}
