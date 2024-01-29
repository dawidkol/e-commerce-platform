package pl.dk.ecommerceplatform.stripe.dtos;

import lombok.Builder;

@Builder
public record PaymentResponse (String message, String url){
}
