package pl.dk.ecommerceplatform.cart.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record AddToCartDto(/*@NotNull @PositiveOrZero Long userId,*/
        @NotNull @PositiveOrZero Long productId,
        @NotNull @PositiveOrZero Long quantity) {
}
