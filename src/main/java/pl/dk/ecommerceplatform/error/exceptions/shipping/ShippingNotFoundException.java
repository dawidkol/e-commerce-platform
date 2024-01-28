package pl.dk.ecommerceplatform.error.exceptions.shipping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Shipping method not found")
public class ShippingNotFoundException extends RuntimeException {
}
