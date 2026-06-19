package dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookRequest(
		@NotBlank String isbn,
        @NotBlank String title,
        @NotBlank String author,
        String genre,
        String description,
        int publishedYear,
        String publisher,
        @Min(0) int totalCopies
) {}
