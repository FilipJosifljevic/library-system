package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoanRequest(
		@NotNull Long userId,
        @NotBlank String bookId
) {}
