package pl.dk.ecommerceplatform.productImage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageValidator implements ConstraintValidator<ImageConstraint, MultipartFile[]> {
    @Override
    public void initialize(ImageConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile[] value, ConstraintValidatorContext context) {
        List<String> imagesMediaType = List.of(
                MediaType.IMAGE_JPEG_VALUE,
                MediaType.IMAGE_PNG_VALUE,
                MediaType.valueOf("image/jpg").toString());
        String supportedMediaTypes = imagesMediaType.stream().reduce("", (first, second) -> first + second + " ");
        boolean valid = false;
        for (MultipartFile file : value) {
            String contentType = file.getContentType();
            context.disableDefaultConstraintViolation();
            String message = "Unsupported media type: %s. Supported types: %s".formatted(contentType, supportedMediaTypes);
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            valid = imagesMediaType.contains(contentType);
        }
        return valid;
    }
}
