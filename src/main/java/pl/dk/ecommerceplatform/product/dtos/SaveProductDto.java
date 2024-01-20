package pl.dk.ecommerceplatform.product.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
public record SaveProductDto(
        @NotBlank
        @Size(min = 3, max = 200)
        String name,
        @NotBlank
        @Size(min = 10, max = 3000)
        String description,
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        @NotNull
        @PositiveOrZero
        Long quantity,
        @NotNull
        @Positive
        Long categoryId,
        @NotNull
        @Positive
        Long brandId,
        @NotNull
        Boolean available
) {
}
