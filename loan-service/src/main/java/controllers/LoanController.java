package controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dtos.LoanRequest;
import dtos.LoanResponse;
import jakarta.validation.Valid;
import services.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	private final LoanService service;

	public LoanController(LoanService service) {
		super();
		this.service = service;
	}
	
	@PostMapping
    public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createLoan(request));
    }

    @PatchMapping("/{id}/return")
    public LoanResponse returnBook(@PathVariable Long id) {
        return service.returnBook(id);
    }

    @GetMapping("/{id}")
    public LoanResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/user/{userId}")
    public List<LoanResponse> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    @GetMapping
    public List<LoanResponse> getAll() {
        return service.getAll();
    }
}
