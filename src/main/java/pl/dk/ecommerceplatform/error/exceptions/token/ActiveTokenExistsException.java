package pl.dk.ecommerceplatform.error.exceptions.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Valid activation link exist in database. Check your email")
public class ActiveTokenExistsException extends RuntimeException {
}
