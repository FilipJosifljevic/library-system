package dtos;

import com.library.model.MembershipStatus;

public record UserResponse(
		Long id,
		String firstName,
		String lastName,
		String email,
		MembershipStatus membershipStatus,
		boolean suspended
) {}
