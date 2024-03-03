package pl.dk.ecommerceplatform.error.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Role not found")
public class RoleNotFoundException extends RuntimeException {
}
