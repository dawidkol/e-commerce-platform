package pl.dk.ecommerceplatform.error.exceptions.address;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UpdateAddressException extends RuntimeException {

    public UpdateAddressException(String message) {
        super(message);
    }
}
