package pl.dk.ecommerceplatform.category.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SaveCategoryDto(@NotBlank @Size(min = 3, max = 100) String name) {
}
