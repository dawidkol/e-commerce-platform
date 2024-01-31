package pl.dk.ecommerceplatform.error;

import lombok.Builder;

@Builder
record ConstraintViolationError(String field, String message) {
}
