package pl.dk.ecommerceplatform.error.exceptions.shipping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ShippingMethodExistException extends RuntimeException {

    public ShippingMethodExistException(String message) {
        super(message);
    }
}