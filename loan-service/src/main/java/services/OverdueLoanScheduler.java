package services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OverdueLoanScheduler {

	private final LoanService loanService;

	public OverdueLoanScheduler(LoanService loanService) {
		super();
		this.loanService = loanService;
	}
	
	@Scheduled(initialDelay=30000, fixedRate=6000)
	public void runOverdueCheck() {
		loanService.checkOverdueLoans();
	}
}
