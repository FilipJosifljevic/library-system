package consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import events.LoanCreatedEvent;
import events.LoanOverdueEvent;
import events.LoanReturnedEvent;
import services.NotificationService;

@Component
public class NotificationConsumer {

	private final NotificationService notificationService;

	public NotificationConsumer(NotificationService notificationService) {
		super();
		this.notificationService = notificationService;
	}
	
	@KafkaListener(topics = "loan-created", groupId = "notification-service-group")
    public void onLoanCreated(LoanCreatedEvent event) {
        notificationService.handleLoanCreated(event);
    }

    @KafkaListener(topics = "loan-returned", groupId = "notification-service-group")
    public void onLoanReturned(LoanReturnedEvent event) {
        notificationService.handleLoanReturned(event);
    }

    @KafkaListener(topics = "loan-overdue", groupId = "notification-service-group")
    public void onLoanOverdue(LoanOverdueEvent event) {
        notificationService.handleLoanOverdue(event);
    }
}
