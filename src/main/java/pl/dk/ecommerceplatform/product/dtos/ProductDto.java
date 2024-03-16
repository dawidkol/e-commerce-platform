package pl.dk.ecommerceplatform.product.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long quantity,
        String category,
        String brand,
        boolean available,
        LocalDate added,
        BigDecimal promotionPrice
) {
}

