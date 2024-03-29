package pl.dk.ecommerceplatform.productImage;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = ImageValidator.class)
@Target({PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageConstraint {

    String message() default "Invalid image file";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
