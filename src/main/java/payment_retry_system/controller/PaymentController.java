package payment_retry_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import payment_retry_system.model.Payment;
import payment_retry_system.service.PaymentService;

@RestController
@RequestMapping("/payments")

public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping
    public Payment create(
            @RequestBody Payment payment
    ) {
        return service.createPayment(payment);
    }

    @GetMapping
    public List<Payment> getAll() {
        return service.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getById(
            @PathVariable Long id
    ) {
        return service.getPayment(id);
    }

    @PutMapping("/{id}")
    public Payment updateStatus(
            @PathVariable Long id,
            @RequestBody Payment payment
    ) {
        return service.updatePayment(id, payment);
    }

    @PutMapping("/{id}/retry")
    public Payment retry(
            @PathVariable Long id
    ) {
        return service.retryPayment(id);
    }


    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        service.deletePayment(id);
    }
}