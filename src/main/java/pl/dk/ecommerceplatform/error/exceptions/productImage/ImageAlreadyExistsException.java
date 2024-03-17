package pl.dk.ecommerceplatform.error.exceptions.productImage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Image already exists")
public class ImageAlreadyExistsException extends RuntimeException {
}
