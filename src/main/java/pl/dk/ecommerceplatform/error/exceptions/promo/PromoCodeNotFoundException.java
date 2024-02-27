package pl.dk.ecommerceplatform.error.exceptions.promo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Promo code not found")
public class PromoCodeNotFoundException extends RuntimeException {

}
