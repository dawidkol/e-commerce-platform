package pl.dk.ecommerceplatform.error.exceptions.address;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Address not found")
public class AddressNotFoundException extends RuntimeException {
}
