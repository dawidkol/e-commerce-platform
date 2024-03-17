package pl.dk.ecommerceplatform.error.exceptions.productImage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Original file name is null")
public class MultipartFilenameException extends RuntimeException {
}
