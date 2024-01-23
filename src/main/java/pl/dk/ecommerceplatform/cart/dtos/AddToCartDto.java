package pl.dk.ecommerceplatform.cart.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record AddToCartDto(@NotBlank @PositiveOrZero Long userId,
                           @NotBlank @PositiveOrZero Long productId,
                           @NotBlank @PositiveOrZero Long quantity) {
}
