package payment_retry_system.service;
import payment_retry_system.model.RetryHistory;
import payment_retry_system.repository.RetryHistoryRepository;
import payment_retry_system.dto.DashboardResponse;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payment_retry_system.model.Payment;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import payment_retry_system.repository.PaymentRepository;

import java.util.List;
import payment_retry_system.model.FailureReason;
import payment_retry_system.model.PaymentStatus;
@Service
public class PaymentService {
    @Autowired
private RetryHistoryRepository historyRepository;

    @Autowired
    private PaymentRepository repository;
    private static final int MAX_RETRY = 3;
    private final ScheduledExecutorService scheduler =
        Executors.newScheduledThreadPool(1);

private final Random random = new Random();

   public Payment createPayment(Payment payment) {

    Payment savedPayment = repository.save(payment);

    if (savedPayment.getStatus() == PaymentStatus.PENDING) {

        scheduler.schedule(() -> {

            Payment dbPayment =
                    repository.findById(savedPayment.getId()).orElse(null);

            if (dbPayment == null) {
                return;
            }

            // Only process if still pending
            if (dbPayment.getStatus() != PaymentStatus.PENDING) {
                return;
            }

            // 70% Success
            if (random.nextInt(100) < 70) {

                dbPayment.setStatus(PaymentStatus.SUCCESS);
                dbPayment.setFailureReason(FailureReason.NONE);

            } else {

                dbPayment.setStatus(PaymentStatus.FAILED);

                FailureReason[] reasons = {
                        FailureReason.NETWORK_ERROR,
                        FailureReason.SERVER_ERROR,
                        FailureReason.BANK_TIMEOUT,
                        FailureReason.CARD_DECLINED,
                        FailureReason.OTP_EXPIRED,
                        FailureReason.INSUFFICIENT_FUNDS
                };

                dbPayment.setFailureReason(
                        reasons[random.nextInt(reasons.length)]
                );
            }

            repository.save(dbPayment);

        }, 10, TimeUnit.SECONDS);

    }

    return savedPayment;
}

    public List<Payment> getAllPayments() {

        List<Payment> payments = repository.findAll();

        payments.sort((a, b) -> b.getId().compareTo(a.getId()));

        return payments;
    }

    public Payment getPayment(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Payment updatePayment(Long id, Payment updated) {

        Payment payment = repository.findById(id).orElse(null);

        if (payment != null) {

            payment.setStatus(updated.getStatus());
            payment.setFailureReason(updated.getFailureReason());
            payment.setRetryCount(updated.getRetryCount());

            return repository.save(payment);
        }

        return null;
    }

    public void deletePayment(Long id) {
        repository.deleteById(id);
    }

    public void retryAllFailed() {

        List<Payment> payments = repository.findAll();

        for (Payment payment : payments) {

            if (payment.getStatus() == PaymentStatus.FAILED) {
                retryPayment(payment.getId());
            }
        }
    }

  public Payment retryPayment(Long id) {

    Payment payment = repository.findById(id).orElse(null);

    if (payment == null) {
        return null;
    }

    if (payment.getStatus() != PaymentStatus.FAILED) {
        return payment;
    }

    payment.setRetryCount(payment.getRetryCount() + 1);

    String message;

    if (payment.getRetryCount() >= MAX_RETRY) {

        payment.setStatus(PaymentStatus.PERMANENT_FAILURE);
        message = "Maximum retry limit reached.";

    } else if (payment.getRetryCount() >= 2) {

        payment.setStatus(PaymentStatus.RECOVERED);
        payment.setFailureReason(FailureReason.NONE);
        message = "Payment recovered successfully.";

    } else {

        payment.setStatus(PaymentStatus.FAILED);
        message = "Retry failed.";

    }

    Payment savedPayment = repository.save(payment);

    saveRetryHistory(savedPayment, message);

    return savedPayment;
}

public List<RetryHistory> getRetryHistory(Long paymentId) {
    return historyRepository.findByPaymentId(paymentId);
}
private void saveRetryHistory(Payment payment, String message) {

    RetryHistory history = new RetryHistory();

    history.setPaymentId(payment.getId());
    history.setRetryNumber(payment.getRetryCount());
    history.setStatus(payment.getStatus());
    history.setFailureReason(payment.getFailureReason());
    history.setMessage(message);
    history.setRetryTime(LocalDateTime.now());

    historyRepository.save(history);
}

public DashboardResponse getDashboard() {

    List<Payment> payments = repository.findAll();

    DashboardResponse dashboard = new DashboardResponse();

    dashboard.setTotalPayments(payments.size());

    dashboard.setPendingPayments(
            payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                    .count());

    dashboard.setSuccessfulPayments(
            payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                    .count());

    dashboard.setFailedPayments(
            payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.FAILED)
                    .count());

    dashboard.setRecoveredPayments(
            payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.RECOVERED)
                    .count());

    dashboard.setPermanentFailures(
            payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.PERMANENT_FAILURE)
                    .count());

    long totalRetries =
            payments.stream()
                    .mapToLong(Payment::getRetryCount)
                    .sum();

    dashboard.setTotalRetries(totalRetries);

    if (dashboard.getFailedPayments() + dashboard.getRecoveredPayments() > 0) {

        double rate =
                (dashboard.getRecoveredPayments() * 100.0)
                /
                (dashboard.getRecoveredPayments()
                + dashboard.getFailedPayments());

        dashboard.setRecoveryRate(rate);

    } else {

        dashboard.setRecoveryRate(0);

    }

    return dashboard;
}


public List<Payment> getPaymentsByStatus(PaymentStatus status) {
    return repository.findByStatus(status);
}

public List<Payment> getPaymentsByFailureReason(FailureReason failureReason) {
    return repository.findByFailureReason(failureReason);
}

public List<Payment> getPaymentsByOrderId(String orderId) {
    return repository.findByOrderId(orderId);
}
}