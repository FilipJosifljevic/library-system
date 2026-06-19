package events;

public record LoanReturnedEvent(
		Long loanId,
        Long userId,
        String bookId,
        String bookTitle
) {}
