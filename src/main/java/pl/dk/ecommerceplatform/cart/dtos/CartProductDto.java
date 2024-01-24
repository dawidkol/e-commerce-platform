package pl.dk.ecommerceplatform.cart.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartProductDto(String name, Long quantity, BigDecimal productPrice, BigDecimal totalPrice) {
}
