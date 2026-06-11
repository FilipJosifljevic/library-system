package dtos;

public record RegisterUserRequest(
		String firstName,
		String lastName,
		String email,
		String password
) {}
