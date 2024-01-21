package pl.dk.ecommerceplatform.error.exceptions.brand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Brand already exists")
public class BrandExistsException extends RuntimeException {
}
