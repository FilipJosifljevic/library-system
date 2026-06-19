package controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dtos.FineResponse;
import services.FineService;

@RestController
@RequestMapping("/api/fines")
public class FineController {

	private final FineService service;

    public FineController(FineService service) {
        this.service = service;
    }

    @GetMapping
    public List<FineResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public FineResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/user/{userId}")
    public List<FineResponse> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    @PatchMapping("/{id}/pay")
    public FineResponse pay(@PathVariable Long id) {
        return service.pay(id);
    }
}
