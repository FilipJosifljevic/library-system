package controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dtos.BookAvailabilityResponse;
import dtos.BookRequest;
import dtos.BookResponse;
import jakarta.validation.Valid;
import services.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
	private final BookService service;

	public BookController(BookService service) {
		super();
		this.service = service;
	}
	
	@PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
	
	@GetMapping
    public List<BookResponse> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre
    ) {
        if (title != null || author != null || genre != null) {
            return service.search(title, author, genre);
        }
        return service.getAll();
    }
	
	@GetMapping("/{id}")
    public BookResponse getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable String id, @Valid @RequestBody BookRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/availability")
    public BookAvailabilityResponse checkAvailability(@PathVariable String id) {
        return service.checkAvailability(id);
    }
    
    @PatchMapping("/{id}/decrease")
    public ResponseEntity<Void> decreaseAvailableCopies(@PathVariable String id) {
        service.decreaseAvailableCopies(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/increase")
    public ResponseEntity<Void> increaseAvailableCopies(@PathVariable String id) {
        service.increaseAvailableCopies(id);
        return ResponseEntity.ok().build();
    }
}
