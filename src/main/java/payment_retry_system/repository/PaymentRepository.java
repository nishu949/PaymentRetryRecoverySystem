package payment_retry_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import payment_retry_system.model.FailureReason;
import payment_retry_system.model.Payment;
import payment_retry_system.model.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByFailureReason(FailureReason failureReason);

    List<Payment> findByOrderId(String orderId);

}