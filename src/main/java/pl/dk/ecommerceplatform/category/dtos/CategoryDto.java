package pl.dk.ecommerceplatform.category.dtos;

import lombok.Builder;

@Builder
public record CategoryDto(Long id, String name) {
}
