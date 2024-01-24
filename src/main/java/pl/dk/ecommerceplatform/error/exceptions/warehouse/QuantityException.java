package pl.dk.ecommerceplatform.error.exceptions.warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Insufficient stock of the product in the warehouse")
public class QuantityException extends RuntimeException {
}
