package payment_retry_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import payment_retry_system.model.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

}