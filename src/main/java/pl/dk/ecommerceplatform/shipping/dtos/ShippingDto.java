package pl.dk.ecommerceplatform.shipping.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ShippingDto(Long id, @NotEmpty String name, @NotNull @PositiveOrZero BigDecimal shippingCost) {
}
