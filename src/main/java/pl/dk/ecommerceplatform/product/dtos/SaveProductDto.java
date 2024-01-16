package pl.dk.ecommerceplatform.product.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
public record SaveProductDto(
        String name,
        String description,
        BigDecimal price,
        Long quantity,
        Long categoryId,
        Long brandId,

        Boolean available,
        LocalDate added
) {
}
