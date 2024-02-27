package pl.dk.ecommerceplatform.error.exceptions.promo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String message) {
        super(message);
    }
}
