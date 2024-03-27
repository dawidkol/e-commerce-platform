package pl.dk.ecommerceplatform.currency;

import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.currency.dtos.CurrencyDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CurrencyDtoMapperTest {


    @Test
    void itShouldCurrencyToCurrencyDto() {
        // Given


        Currency euro = Currency.builder()
                .id(1L)
                .name("euro")
                .code(CurrencyCode.EUR)
                .effectiveDate(LocalDate.now())
                .bid(BigDecimal.valueOf(4.00))
                .ask(BigDecimal.valueOf(4.15))
                .build();

        // When
        CurrencyDto currencyDto = CurrencyDtoMapper.map(euro);

        // Then
        assertAll(
                () -> assertThat(currencyDto.id()).isEqualTo(euro.getId()),
                () -> assertThat(currencyDto.name()).isEqualTo(euro.getName()),
                () -> assertThat(currencyDto.code()).isEqualTo(euro.getCode().name()),
                () -> assertThat(currencyDto.effectiveDate()).isEqualTo(euro.getEffectiveDate()),
                () -> assertThat(currencyDto.bid()).isEqualTo(euro.getBid()),
                () -> assertThat(currencyDto.ask()).isEqualTo(euro.getAsk())
        );
    }
}