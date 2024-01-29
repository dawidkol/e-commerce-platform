package pl.dk.ecommerceplatform.error.exceptions.stripe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserNotMatchException extends RuntimeException {
    public UserNotMatchException(String message) {
        super(message);
    }
}
