package events;

import java.time.LocalDate;

public record LoanOverdueEvent(
		Long loanId,
        Long userId,
        String bookId,
        String bookTitle,
        LocalDate dueDate,
        long daysOverdue
) {}
