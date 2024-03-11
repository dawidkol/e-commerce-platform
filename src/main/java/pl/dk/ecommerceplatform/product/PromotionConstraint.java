package pl.dk.ecommerceplatform.product;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = PromotionValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PromotionConstraint {
    String message() default "Promotion price must be lower than regular price";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
