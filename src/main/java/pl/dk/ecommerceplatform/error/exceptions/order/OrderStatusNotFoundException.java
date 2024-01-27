package pl.dk.ecommerceplatform.error.exceptions.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order status not found")
public class OrderStatusNotFoundException extends RuntimeException {
}
