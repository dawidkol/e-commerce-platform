package pl.dk.ecommerceplatform.stripe;

import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.dk.ecommerceplatform.security.SecurityService;
import pl.dk.ecommerceplatform.stripe.dtos.PaymentResponse;
import pl.dk.ecommerceplatform.stripe.dtos.StripePaymentDto;

import java.util.List;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
class PaymentController {

    private final PaymentService paymentService;
    private final SecurityService securityService;

    @PostMapping("/{orderId}")
    @PreAuthorize(value = "hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable Long orderId, @RequestParam(name = "code", required = false, defaultValue = "PLN") String currencyCode) {
        String emailFromSecurityContext = securityService.getEmailFromSecurityContext();
        PaymentResponse payment = paymentService.createPayment(orderId, emailFromSecurityContext, currencyCode);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/refund/{orderId}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getList(@PathVariable Long orderId) {
        paymentService.refund(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refunds")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<StripePaymentDto>> getAllPaymentsToRefund() {
        List<StripePaymentDto> allPaymentsToRefund = paymentService.getAllPaymentsToRefund();
        return ResponseEntity.ok(allPaymentsToRefund);
    }
}
