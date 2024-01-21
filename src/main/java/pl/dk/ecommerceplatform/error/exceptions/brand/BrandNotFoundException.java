package pl.dk.ecommerceplatform.error.exceptions.brand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Brand not found")
public class BrandNotFoundException extends RuntimeException {
}
