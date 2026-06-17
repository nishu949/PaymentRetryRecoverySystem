package payment_retry_system.schedular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import payment_retry_system.service.PaymentService;

@Component
public class RetryScheduler {

    @Autowired
    private PaymentService service;

    @Scheduled(fixedRate = 30000)
    public void retryFailedPayments() {

        service.retryAllFailed();

        System.out.println(
                "Automatic retry executed"
        );
    }
}