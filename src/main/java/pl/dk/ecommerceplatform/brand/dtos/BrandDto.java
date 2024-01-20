package pl.dk.ecommerceplatform.brand.dtos;

import lombok.Builder;

@Builder
public record BrandDto(Long id, String name) {
}
