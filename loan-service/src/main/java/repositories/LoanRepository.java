package repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import models.Loan;
import models.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {
	List<Loan> findByUserId(Long userId);
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findByStatusAndDueDateBefore(LoanStatus status, LocalDate date);
}
