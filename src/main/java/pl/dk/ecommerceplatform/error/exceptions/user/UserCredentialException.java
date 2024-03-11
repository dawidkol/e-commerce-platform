package pl.dk.ecommerceplatform.error.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid credentials. Check email and password.")
public class UserCredentialException extends RuntimeException {
}
