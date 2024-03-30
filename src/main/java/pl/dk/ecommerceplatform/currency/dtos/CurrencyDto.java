package pl.dk.ecommerceplatform.currency.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CurrencyDto(Long id, String name, String code, LocalDate effectiveDate, BigDecimal bid, BigDecimal ask) {
}
