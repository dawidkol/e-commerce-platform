package pl.dk.ecommerceplatform.error.exceptions.stripe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }
}
