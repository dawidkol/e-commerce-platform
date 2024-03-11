package pl.dk.ecommerceplatform.error.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User account not activated")
public class UserNotActivatedException extends RuntimeException {
}
