package repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import models.Fine;
import models.FineStatus;

public interface FineRepository extends JpaRepository<Fine, Long>{
	List<Fine> findByUserId(Long userId);
    List<Fine> findByStatus(FineStatus status);
    Optional<Fine> findByLoanId(Long loanId);
}
