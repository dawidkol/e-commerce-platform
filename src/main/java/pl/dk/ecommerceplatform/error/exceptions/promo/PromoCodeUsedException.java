package pl.dk.ecommerceplatform.error.exceptions.promo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Promo code used")
public class PromoCodeUsedException extends RuntimeException {
}
