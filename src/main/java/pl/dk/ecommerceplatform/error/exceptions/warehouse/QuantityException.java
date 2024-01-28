package pl.dk.ecommerceplatform.error.exceptions.warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class QuantityException extends RuntimeException {

    public QuantityException(String message) {
        super(message);
    }
}
