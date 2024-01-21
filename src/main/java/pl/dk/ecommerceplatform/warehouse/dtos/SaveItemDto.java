package pl.dk.ecommerceplatform.warehouse.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record SaveItemDto(
        Long id,
        @NotNull @PositiveOrZero Long productId,
        @NotNull @PositiveOrZero Long quantity,
        @NotNull boolean available) {
}
