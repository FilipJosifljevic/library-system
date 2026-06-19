package consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import events.LoanOverdueEvent;
import services.FineService;

@Component
public class FineConsumer {

	private final FineService fineService;
	
	public FineConsumer(FineService fineService) {
        this.fineService = fineService;
    }

    @KafkaListener(topics = "loan-overdue", groupId = "fine-service-group")
    public void onLoanOverdue(LoanOverdueEvent event) {
        fineService.createFineFromEvent(event);
    }
}
