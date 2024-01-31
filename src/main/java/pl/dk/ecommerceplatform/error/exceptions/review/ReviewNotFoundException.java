package pl.dk.ecommerceplatform.error.exceptions.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Review not fount")
public class ReviewNotFoundException extends RuntimeException {
}
