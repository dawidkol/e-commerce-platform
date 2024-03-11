package pl.dk.ecommerceplatform.error.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The account has already been activated ")
public class AccountAlreadyActivatedException extends RuntimeException{
}
