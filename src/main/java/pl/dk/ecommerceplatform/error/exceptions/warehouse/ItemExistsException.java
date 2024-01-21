package pl.dk.ecommerceplatform.error.exceptions.warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Item exists in warehouse")
public class ItemExistsException extends RuntimeException {
}
