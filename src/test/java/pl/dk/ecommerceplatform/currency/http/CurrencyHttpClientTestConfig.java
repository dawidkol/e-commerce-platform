package pl.dk.ecommerceplatform.currency.http;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import pl.dk.ecommerceplatform.currency.CurrencyRepository;

@TestConfiguration
class CurrencyHttpClientTestConfig {

    @Bean
    public CurrencyRepository currencyRepository() {
        return Mockito.mock(CurrencyRepository.class);
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }
}
