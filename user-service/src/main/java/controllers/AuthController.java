package controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dtos.AuthResponse;
import dtos.LoginRequest;
import services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationService service;

	public AuthController(AuthenticationService service) {
		super();
		this.service = service;
	}
	
	@PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request) {

        return service.login(request);
    }
}
