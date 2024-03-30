package pl.dk.ecommerceplatform.currency;

import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.error.exceptions.currency.CurrencyNotFoundException;

import java.util.Set;

import static com.stripe.param.checkout.SessionCreateParams.PaymentMethodType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyCodeTest {

    private CurrencyCode underTest;

    @Test
    void itShouldGetUSDCurrency() {
        // Given
        String USDStringCode = "USD";

        // When
        CurrencyCode currency = CurrencyCode.getCurrency(USDStringCode);

        // Then
        assertAll(
                () -> assertThat(currency.name()).isEqualTo(USDStringCode),
                () -> assertThat(currency.getPaymentMethods()).contains(PAYPAL, CARD)
        );
    }

    @Test
    void itShouldThrowCurrencyNotFoundExceptionWhenCurrencyCodeIsNotSupported() {
        // Given
        String notSupportedStringCode = "ABC";

        // When
        // Then
        assertThrows(CurrencyNotFoundException.class, () -> CurrencyCode.getCurrency(notSupportedStringCode)
        );
    }
}