package pl.dk.ecommerceplatform.stripe;

import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dk.ecommerceplatform.security.SecurityService;
import pl.dk.ecommerceplatform.stripe.dtos.PaymentResponse;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
class PaymentController {

    private final PaymentService paymentService;
    private final SecurityService securityService;

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable Long orderId) throws StripeException {
        String emailFromSecurityContext = securityService.getEmailFromSecurityContext();
        PaymentResponse payment = paymentService.createPayment(orderId, emailFromSecurityContext);
        return ResponseEntity.ok(payment);
    }
}
