package pl.dk.ecommerceplatform.shipping.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ShippingDto(Long id, String name, BigDecimal shippingCost) {
}
