package pl.dk.ecommerceplatform.currency.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import pl.dk.ecommerceplatform.currency.CurrencyCode;


@Builder
public record CurrencyReceiver(
        @JsonProperty("currency")
        String name,
        CurrencyCode code,
        @JsonProperty("rates")
        RatesReceiver[] rates
) {

}

