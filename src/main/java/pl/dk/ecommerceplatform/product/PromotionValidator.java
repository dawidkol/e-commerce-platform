package pl.dk.ecommerceplatform.product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PromotionValidator implements ConstraintValidator<PromotionConstraint, Product> {

    @Override
    public void initialize(PromotionConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Product value, ConstraintValidatorContext context) {
        if (value.getPromotionPrice() == null) {
            return true;
        } else return value.getPrice().compareTo(value.getPromotionPrice()) > 0;
    }
}
