package controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.model.MembershipStatus;

import dtos.RegisterUserRequest;
import dtos.UpdateUserRequest;
import dtos.UserResponse;
import services.UserService;

@RestController
@RequestMapping("/api/users")

public class UserController {
	private final UserService service;

    public UserController(UserService service) {
		super();
		this.service = service;
	}

	@PostMapping("/register")
    public UserResponse register(
            @RequestBody RegisterUserRequest request) {

        return service.register(request);
    }

    @GetMapping("/{id}")
    public UserResponse getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        return service.update(id, request);
    }

    @PutMapping("/{id}/suspend")
    public void suspend(
            @PathVariable Long id) {

        service.suspend(id);
    }

    @GetMapping("/{id}/membership")
    public MembershipStatus membership(
            @PathVariable Long id) {

        return service.checkMembership(id);
    }
}
