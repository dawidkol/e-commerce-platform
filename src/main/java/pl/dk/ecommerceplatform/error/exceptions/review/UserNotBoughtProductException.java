package pl.dk.ecommerceplatform.error.exceptions.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User has not bought the product")
public class UserNotBoughtProductException extends RuntimeException {
}
