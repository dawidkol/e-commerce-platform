package pl.dk.ecommerceplatform.currency.http;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RatesReceiver(LocalDate effectiveDate, BigDecimal ask, BigDecimal bid) {
}
