package pl.dk.ecommerceplatform.error;

import lombok.Builder;

@Builder
record SqlError(String message, String error) {
}
