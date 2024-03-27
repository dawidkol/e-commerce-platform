package pl.dk.ecommerceplatform.currency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.currency.dtos.CurrencyDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;
    private CurrencyService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CurrencyServiceImpl(currencyRepository);

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldReturnAllCurrencies() {
        // Given

        Currency euro = Currency.builder()
                .id(1L)
                .name("euro")
                .code(CurrencyCode.EUR)
                .effectiveDate(LocalDate.now())
                .bid(BigDecimal.valueOf(4.00))
                .ask(BigDecimal.valueOf(4.15))
                .build();

        Currency zloty = Currency.builder()
                .id(1L)
                .name("polski złotu")
                .code(CurrencyCode.PLN)
                .effectiveDate(LocalDate.now())
                .bid(BigDecimal.valueOf(1.00))
                .ask(BigDecimal.valueOf(1.00))
                .build();

        when(currencyRepository.findAll()).thenReturn(List.of(euro, zloty));

        // When
        List<CurrencyDto> currencies = underTest.getAllCurrencies();

        // Then
        verify(currencyRepository, times(1)).findAll();
        assertAll(
                () -> assertThat(currencies.size()).isEqualTo(2)
        );
    }
}