package pl.dk.ecommerceplatform.error.exceptions.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Order already exists")
public class OrderAlreadyExistsException extends RuntimeException {
}
