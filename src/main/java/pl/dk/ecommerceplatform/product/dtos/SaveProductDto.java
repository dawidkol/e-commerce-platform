package pl.dk.ecommerceplatform.product.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record SaveProductDto(
        Long id,
        @NotBlank
        @Size(min = 3, max = 100)
        String name,
        @NotBlank
        @Size(min = 10, max = 3000)
        String description,
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        @NotNull
        @Positive
        Long categoryId,
        @NotNull
        @Positive
        Long brandId,
        BigDecimal promotionPrice
) {
}
