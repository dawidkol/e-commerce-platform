package pl.dk.ecommerceplatform.currency.http;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record RatesReceiver(LocalDate effectiveDate, BigDecimal ask, BigDecimal bid) {
}
