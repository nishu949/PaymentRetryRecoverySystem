package payment_retry_system.controller;
import payment_retry_system.dto.DashboardResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import payment_retry_system.model.Payment;
import payment_retry_system.model.RetryHistory;
import payment_retry_system.service.PaymentService;
import payment_retry_system.model.PaymentStatus;
import payment_retry_system.model.FailureReason;
@RestController
@RequestMapping("/payments")
@Tag(name = "Payment Management", description = "Payment Retry & Recovery APIs")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @Operation(summary = "Create a new payment")
    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        return service.createPayment(payment);
    }

    @Operation(summary = "Get all payments")
    @GetMapping
    public List<Payment> getAll() {
        return service.getAllPayments();
    }
   
// @GetMapping("/dashboard")
// public DashboardResponse getDashboard() {
//     return service.getDashboard();
// }

    @Operation(summary = "Get payment by ID")
    @GetMapping("/{id}")
    public Payment getById(@PathVariable Long id) {
        return service.getPayment(id);
    }

    @Operation(summary = "Update payment")
    @PutMapping("/{id}")
    public Payment updateStatus(
            @PathVariable Long id,
            @RequestBody Payment payment) {

        return service.updatePayment(id, payment);
    }

    @Operation(summary = "Retry failed payment")
    @PutMapping("/{id}/retry")
    public Payment retry(@PathVariable Long id) {
        return service.retryPayment(id);
    }

    @Operation(summary = "View retry history")
    @GetMapping("/{id}/history")
    public List<RetryHistory> getRetryHistory(@PathVariable Long id) {
        return service.getRetryHistory(id);
    }

    @Operation(summary = "Delete payment")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePayment(id);
    }
    @Operation(summary = "Get payments by status")
@GetMapping("/status/{status}")
public List<Payment> getByStatus(
        @PathVariable PaymentStatus status) {

    return service.getPaymentsByStatus(status);
}
@Operation(summary = "Get payments by failure reason")
@GetMapping("/failure-reason/{reason}")
public List<Payment> getByFailureReason(
        @PathVariable FailureReason reason) {

    return service.getPaymentsByFailureReason(reason);
}
@Operation(summary = "Get payment by order ID")
@GetMapping("/order/{orderId}")
public List<Payment> getByOrderId(
        @PathVariable String orderId) {

    return service.getPaymentsByOrderId(orderId);
}
}