package payment_retry_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import payment_retry_system.dto.DashboardResponse;
import payment_retry_system.repository.PaymentRepository;
import payment_retry_system.repository.RetryHistoryRepository;
import payment_retry_system.model.Payment;
import payment_retry_system.model.PaymentStatus;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {


    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    private RetryHistoryRepository retryHistoryRepository;



    @GetMapping
    public DashboardResponse getDashboard(){


        List<Payment> payments = paymentRepository.findAll();


        long total = payments.size();


        long successful = payments.stream()
                .filter(p -> p.getStatus()==PaymentStatus.SUCCESS)
                .count();


        long failed = payments.stream()
                .filter(p -> p.getStatus()==PaymentStatus.FAILED)
                .count();


        long recovered = payments.stream()
                .filter(p -> p.getStatus()==PaymentStatus.RECOVERED)
                .count();



        long permanentFailure = payments.stream()
                .filter(p -> p.getStatus()==PaymentStatus.PERMANENT_FAILURE)
                .count();



        long retries = retryHistoryRepository.count();



        double recoveryRate = total == 0 ?
                0 :
                ((double)recovered / total) * 100;



        return new DashboardResponse(
                total,
                0,
                successful,
                failed,
                recovered,
                permanentFailure,
                retries,
                recoveryRate
        );

    }

}