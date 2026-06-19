package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import clients.BookClient;
import clients.UserClient;
import dtos.BookAvailabilityResponse;
import dtos.LoanRequest;
import dtos.LoanResponse;
import dtos.MembershipResponse;
import events.LoanCreatedEvent;
import events.LoanOverdueEvent;
import events.LoanReturnedEvent;
import exceptions.LoanNotAllowedException;
import exceptions.LoanNotFoundException;
import models.Loan;
import models.LoanStatus;
import repositories.LoanRepository;

@Service
public class LoanService {
	private static final int LOAN_PERIOD_DAYS = 14;
	
	private final LoanRepository repository;
	private final BookClient bookClient;
	private final UserClient userClient;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	
	public LoanService(LoanRepository repository, BookClient bookClient, UserClient userClient,
			KafkaTemplate<String, Object> kafkaTemplate) {
		super();
		this.repository = repository;
		this.bookClient = bookClient;
		this.userClient = userClient;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public LoanResponse createLoan(LoanRequest request) {
  
        MembershipResponse membership = userClient.getMembership(request.userId());

        if (membership.suspended() || !"ACTIVE".equals(membership.membershipStatus())) {
            throw new LoanNotAllowedException(
                    "User membership is not active (status: " + membership.membershipStatus() + ")");
        }

        // 2. Check book availability
        BookAvailabilityResponse availability = bookClient.checkAvailability(request.bookId());

        if (!availability.available()) {
            throw new LoanNotAllowedException("Book is not available for loan: " + availability.title());
        }

        // 3. Create loan
        Loan loan = new Loan();
        loan.setUserId(request.userId());
        loan.setBookId(request.bookId());
        loan.setBookTitle(availability.title());
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));
        loan.setStatus(LoanStatus.ACTIVE);

        repository.save(loan);

        // 4. Decrease available copies
        bookClient.decreaseAvailableCopies(request.bookId());

        // 5. Publish event
        kafkaTemplate.send("loan-created", new LoanCreatedEvent(
                loan.getId(), loan.getUserId(), loan.getBookId(), loan.getBookTitle(),
                loan.getLoanDate(), loan.getDueDate()
        ));

        return map(loan);
    }
	
	public LoanResponse returnBook(Long loanId) {
        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new LoanNotAllowedException("Loan is already returned");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);
        repository.save(loan);

        bookClient.increaseAvailableCopies(loan.getBookId());

        kafkaTemplate.send("loan-returned", new LoanReturnedEvent(
                loan.getId(), loan.getUserId(), loan.getBookId(), loan.getBookTitle()
        ));

        return map(loan);
    }
	
	public LoanResponse getById(Long id) {
        return map(repository.findById(id).orElseThrow(() -> new LoanNotFoundException(id)));
    }

    public List<LoanResponse> getByUser(Long userId) {
        return repository.findByUserId(userId).stream().map(this::map).toList();
    }

    public List<LoanResponse> getAll() {
        return repository.findAll().stream().map(this::map).toList();
    }
    
    public void checkOverdueLoans() {
        List<Loan> overdue = repository.findByStatusAndDueDateBefore(LoanStatus.ACTIVE, LocalDate.now());

        for (Loan loan : overdue) {
            loan.setStatus(LoanStatus.OVERDUE);
            repository.save(loan);

            long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());

            kafkaTemplate.send("loan-overdue", new LoanOverdueEvent(
                    loan.getId(), loan.getUserId(), loan.getBookId(), loan.getBookTitle(),
                    loan.getDueDate(), daysOverdue
            ));
        }
    }
    
    private LoanResponse map(Loan loan) {
        return new LoanResponse(
                loan.getId(), loan.getUserId(), loan.getBookId(), loan.getBookTitle(),
                loan.getLoanDate(), loan.getDueDate(), loan.getReturnDate(), loan.getStatus()
        );
    }
}
