package pl.dk.ecommerceplatform.error.exceptions.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Category already exists")
public class CategoryExistsException extends RuntimeException {
}
