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
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable Long orderId, @RequestParam(name = "code", required = false, defaultValue = "PLN") String currencyCode) throws StripeException {
        String emailFromSecurityContext = securityService.getEmailFromSecurityContext();
        PaymentResponse payment = paymentService.createPayment(orderId, emailFromSecurityContext, currencyCode);
        return ResponseEntity.ok(payment);
    }
}
