package pl.dk.ecommerceplatform.warehouse.dtos;

import lombok.Builder;

@Builder
public record SaveItemDto(Long productId, Long quantity, boolean available) {
}
