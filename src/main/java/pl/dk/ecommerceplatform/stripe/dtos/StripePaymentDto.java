package pl.dk.ecommerceplatform.stripe.dtos;

import lombok.Builder;

@Builder
public record StripePaymentDto(String id, Long orderId, String customerEmail, Boolean refund, Boolean refunded) {
}
