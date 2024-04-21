package pl.dk.ecommerceplatform.error.exceptions.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class OrderCancelledException extends RuntimeException {

    public OrderCancelledException(String message) {
        super(message);
    }
}
