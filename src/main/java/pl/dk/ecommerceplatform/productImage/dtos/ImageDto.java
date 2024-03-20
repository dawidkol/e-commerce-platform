package pl.dk.ecommerceplatform.productImage.dtos;

import lombok.Builder;

@Builder
public record ImageDto(Long id, String name, String type, String filePatch, Long productId) {
}
