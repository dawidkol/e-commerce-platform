package pl.dk.ecommerceplatform.error.exceptions.productImage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ImageFilePatchNotFoundException extends RuntimeException {

    public ImageFilePatchNotFoundException(String message) {
        super(message);
    }
}
