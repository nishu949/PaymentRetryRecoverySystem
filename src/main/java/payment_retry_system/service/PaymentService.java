package payment_retry_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import payment_retry_system.model.Payment;
import payment_retry_system.repository.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    public Payment createPayment(
            Payment payment
    ) {
        return repository.save(payment);
    }

   public List<Payment> getAllPayments() {

List<Payment> payments =
repository.findAll();

payments.sort(
(a,b)->
b.getId()
.compareTo(
a.getId()
)
);

return payments;

}

    public Payment getPayment(
            Long id
    ) {
        return repository.findById(id)
                .orElse(null);
    }

    public Payment updatePayment(
            Long id,
            Payment updated
    ) {

        Payment payment =
                repository.findById(id)
                        .orElse(null);

        if (payment != null) {

            payment.setStatus(
                    updated.getStatus()
            );

            payment.setRetryCount(
                    updated.getRetryCount()
            );

            return repository.save(payment);
        }

        return null;
    }
    public void deletePayment(
        Long id
) {
    repository.deleteById(id);
}
public void retryAllFailed() {

    List<Payment> payments =
            repository.findAll();

    for (
            Payment payment
            :
            payments
    ) {

        if (
                "FAILED".equals(
                        payment.getStatus()
                )
        ) {

            retryPayment(
                    payment.getId()
            );

        }

    }

}

public Payment retryPayment(Long id) {

    Payment payment =
            repository.findById(id)
                    .orElse(null);

    if(payment == null){
        return null;
    }

    // allow retry only for failed
    if(
        !"FAILED".equals(
                payment.getStatus()
        )
    ){
        return payment;
    }

    // increase retry
    payment.setRetryCount(
            payment.getRetryCount() + 1
    );

    // recovery success immediately
    payment.setStatus(
            "SUCCESS"
    );

    return repository.save(
            payment
    );
}
}