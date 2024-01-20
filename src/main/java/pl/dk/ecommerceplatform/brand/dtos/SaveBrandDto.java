package pl.dk.ecommerceplatform.brand.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SaveBrandDto(@NotBlank @Size(min = 3, max = 50) String name) {
}
