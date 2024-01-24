package pl.dk.ecommerceplatform.error.exceptions.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cart not found")
public class CartNotFoundException extends RuntimeException {
}
