package dtos;

public record BookAvailabilityResponse(
		String bookId,
        String title,
        int availableCopies,
        boolean available
) {}
