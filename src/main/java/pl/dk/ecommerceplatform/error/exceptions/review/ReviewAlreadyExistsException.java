package pl.dk.ecommerceplatform.error.exceptions.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Review already exists")
public class ReviewAlreadyExistsException extends RuntimeException {
}
