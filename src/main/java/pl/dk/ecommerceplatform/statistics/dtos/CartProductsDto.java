package pl.dk.ecommerceplatform.statistics.dtos;

import lombok.Builder;

@Builder
public record CartProductsDto(String productName, Long amountOfSoldProducts, String url) {
}
