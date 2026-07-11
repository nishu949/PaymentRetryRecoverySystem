package payment_retry_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment_retry_system.model.RetryHistory;

import java.util.List;

public interface RetryHistoryRepository extends JpaRepository<RetryHistory, Long> {

    List<RetryHistory> findByPaymentId(Long paymentId);

}