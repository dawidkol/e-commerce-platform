package pl.dk.ecommerceplatform.currency.http;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import pl.dk.ecommerceplatform.currency.Currency;
import pl.dk.ecommerceplatform.currency.CurrencyCode;
import pl.dk.ecommerceplatform.currency.CurrencyRepository;

import java.time.Duration;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@RestClientTest(value = CurrencyHttpClient.class)
@ContextConfiguration(classes = CurrencyHttpClientTestConfig.class)
class CurrencyHttpClientTest {

    @Autowired
    private CurrencyHttpClient underTest;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void itShouldFetchAllCurrencies() {
        // Given
        String response = """
                {
                  "table": "C",
                  "currency": "dolar amerykański",
                  "code": "USD",
                  "rates": [
                    {
                      "no": "064/C/NBP/2024",
                      "effectiveDate": "2024-03-29",
                      "bid": 3.9507,
                      "ask": 4.0305
                    }
                  ]
                }
                """;

        CurrencyCode[] values = CurrencyCode.values();
        for (CurrencyCode value : values) {
            if (value.equals(CurrencyCode.PLN))
                continue;
            server.expect(requestTo("http://api.nbp.pl/api/exchangerates/rates/c/%s?format=json".formatted(value.name())))
                    .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_JSON));
        }
        // When
        underTest.retrieveAndSaveCurrencyValues();

        // Then
        server.verify();

    }
}