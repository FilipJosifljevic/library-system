package events;

import java.time.LocalDate;

public record LoanCreatedEvent(
		Long loanId,
        Long userId,
        String bookId,
        String bookTitle,
        LocalDate loanDate,
        LocalDate dueDate
) {}
