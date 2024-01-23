package pl.dk.ecommerceplatform.cart.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartDto (Long id, Long userId, List<CartProductDto> products, BigDecimal currentCartValue) {

}
