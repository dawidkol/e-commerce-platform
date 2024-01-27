package pl.dk.ecommerceplatform.order.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record SaveOrderDto(@NotNull @Positive Long cartId,
                           @NotNull @Positive Long shippingId,
                           @NotNull @Positive Long addressId) {
}
