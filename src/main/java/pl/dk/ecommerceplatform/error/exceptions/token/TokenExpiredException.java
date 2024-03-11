package pl.dk.ecommerceplatform.error.exceptions.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The provided token expired")
public class TokenExpiredException extends RuntimeException {
}
