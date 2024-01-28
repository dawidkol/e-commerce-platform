package pl.dk.ecommerceplatform.warehouse.dtos;

import lombok.Builder;

@Builder
public record ItemDto(Long id, String productName, Long quantity, boolean available) {
}
