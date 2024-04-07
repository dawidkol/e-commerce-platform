package pl.dk.ecommerceplatform.shipping.dtos;

import java.math.BigDecimal;

public record UpdateShippingPriceDto(BigDecimal newPrice) {
}
