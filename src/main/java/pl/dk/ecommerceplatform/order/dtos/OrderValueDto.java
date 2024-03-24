package pl.dk.ecommerceplatform.order.dtos;

import lombok.Builder;
import pl.dk.ecommerceplatform.currency.CurrencyCode;

import java.math.BigDecimal;

@Builder
public record OrderValueDto(Long id, CurrencyCode currencyCode, BigDecimal orderValue) {
}
