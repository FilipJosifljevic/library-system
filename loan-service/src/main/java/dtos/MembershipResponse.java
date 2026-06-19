package dtos;

public record MembershipResponse(
		Long userId,
        String membershipStatus,
        boolean suspended
){}
