package dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import models.FineStatus;

public record FineResponse(
		Long id,
        Long loanId,
        Long userId,
        String bookId,
        String bookTitle,
        LocalDate dueDate,
        long daysOverdue,
        BigDecimal amount,
        FineStatus status,
        LocalDate createdDate,
        LocalDate paidDate
) {}
