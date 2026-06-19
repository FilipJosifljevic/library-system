package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import clients.UserClient;
import dtos.UserResponse;
import events.LoanCreatedEvent;
import events.LoanOverdueEvent;
import events.LoanReturnedEvent;

@Service
public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final UserClient userClient;
    private final EmailService emailService;
    
	public NotificationService(UserClient userClient, EmailService emailService) {
		super();
		this.userClient = userClient;
		this.emailService = emailService;
	}
    
	public void handleLoanCreated(LoanCreatedEvent event) {
        UserResponse user = userClient.getUser(event.userId());

        String subject = "Loan confirmation - " + event.bookTitle();
        String body = "Hi " + user.firstName() + ",\n\n"
                + "You have successfully borrowed \"" + event.bookTitle() + "\".\n"
                + "Loan date: " + event.loanDate() + "\n"
                + "Due date: " + event.dueDate() + "\n\n"
                + "Please return it on time to avoid fines.";

        log.info("NOTIFICATION [LOAN CREATED] -> userId={}, email={}, book='{}', due={}",
                event.userId(), user.email(), event.bookTitle(), event.dueDate());

        emailService.send(user.email(), subject, body);
    }
	
	public void handleLoanReturned(LoanReturnedEvent event) {
        UserResponse user = userClient.getUser(event.userId());

        String subject = "Return confirmation - " + event.bookTitle();
        String body = "Hi " + user.firstName() + ",\n\n"
                + "Thank you for returning \"" + event.bookTitle() + "\".";

        log.info("NOTIFICATION [LOAN RETURNED] -> userId={}, email={}, book='{}'",
                event.userId(), user.email(), event.bookTitle());

        emailService.send(user.email(), subject, body);
    }

    public void handleLoanOverdue(LoanOverdueEvent event) {
        UserResponse user = userClient.getUser(event.userId());

        String subject = "OVERDUE - " + event.bookTitle();
        String body = "Hi " + user.firstName() + ",\n\n"
                + "The book \"" + event.bookTitle() + "\" was due on " + event.dueDate() + " "
                + "and is now " + event.daysOverdue() + " day(s) overdue.\n"
                + "Please return it as soon as possible to avoid further fines.";

        log.warn("NOTIFICATION [LOAN OVERDUE] -> userId={}, email={}, book='{}', daysOverdue={}",
                event.userId(), user.email(), event.bookTitle(), event.daysOverdue());

        emailService.send(user.email(), subject, body);
    }
}
