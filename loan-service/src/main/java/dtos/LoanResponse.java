package dtos;

import java.time.LocalDate;

import models.LoanStatus;

public record LoanResponse(
		Long id,
        Long userId,
        String bookId,
        String bookTitle,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        LoanStatus status
) {}
