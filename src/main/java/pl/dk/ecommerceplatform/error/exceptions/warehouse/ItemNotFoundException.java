package pl.dk.ecommerceplatform.error.exceptions.warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item not found")
public class ItemNotFoundException extends RuntimeException {
}
