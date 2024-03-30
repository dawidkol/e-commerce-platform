package pl.dk.ecommerceplatform.currency;

import com.stripe.param.checkout.SessionCreateParams.PaymentMethodType;
import lombok.Getter;
import pl.dk.ecommerceplatform.error.exceptions.currency.CurrencyNotFoundException;

import java.util.Set;

import static com.stripe.param.checkout.SessionCreateParams.PaymentMethodType.*;

@Getter
public enum CurrencyCode {
    PLN(Set.of(BLIK, P24, PAYPAL, CARD)),
    EUR(Set.of(P24, PAYPAL, CARD)),
    USD(Set.of(PAYPAL, CARD)),
    GBP(Set.of(PAYPAL, CARD));

    private final Set<PaymentMethodType> paymentMethods;

    CurrencyCode(Set<PaymentMethodType> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public static CurrencyCode getCurrency(String code) {
        CurrencyCode currencyCode;
        try {
            currencyCode = Enum.valueOf(CurrencyCode.class, code.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new CurrencyNotFoundException("Currency code: %s not found".formatted(code));
        }
        return currencyCode;
    }
}
