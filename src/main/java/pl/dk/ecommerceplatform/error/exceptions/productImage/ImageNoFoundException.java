package pl.dk.ecommerceplatform.error.exceptions.productImage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageNoFoundException extends RuntimeException {

    public ImageNoFoundException(String message) {
        super(message);
    }
}
