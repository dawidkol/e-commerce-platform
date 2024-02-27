package pl.dk.ecommerceplatform.error.exceptions.promo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Promo code expired")
public class PromoCodeExpiredException extends RuntimeException {
}
