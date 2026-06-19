package dtos;

public record BookResponse(
		String id,
        String isbn,
        String title,
        String author,
        String genre,
        String description,
        int publishedYear,
        String publisher,
        int totalCopies,
        int availableCopies
) {}
