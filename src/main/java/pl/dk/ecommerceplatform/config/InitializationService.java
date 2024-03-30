package pl.dk.ecommerceplatform.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import pl.dk.ecommerceplatform.currency.http.CurrencyHttpClient;

@Component
@AllArgsConstructor
class InitializationService implements InitializingBean {

    private final CurrencyHttpClient currencyHttpClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        currencyHttpClient.retrieveAndSaveCurrencyValues();
    }
}
