package pl.dk.ecommerceplatform.product.dtos;

import lombok.Builder;
import pl.dk.ecommerceplatform.brand.Brand;
import pl.dk.ecommerceplatform.category.Category;

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
        Boolean available,
        LocalDate added
) {
}

