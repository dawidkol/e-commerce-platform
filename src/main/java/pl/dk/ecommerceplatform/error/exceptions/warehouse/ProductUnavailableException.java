package pl.dk.ecommerceplatform.error.exceptions.warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Sales of this product have been suspended")
public class ProductUnavailableException extends RuntimeException {
}
