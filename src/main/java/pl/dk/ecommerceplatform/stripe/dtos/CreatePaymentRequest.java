package pl.dk.ecommerceplatform.stripe.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreatePaymentRequest(String customerEmail, BigDecimal amount) {
}
