package pl.dk.ecommerceplatform.currency.http;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.dk.ecommerceplatform.currency.CurrencyCode;
import pl.dk.ecommerceplatform.currency.Currency;
import pl.dk.ecommerceplatform.currency.CurrencyRepository;
import pl.dk.ecommerceplatform.utils.UtilsService;


@Service
@AllArgsConstructor
class CurrencyHttpClient {

    private final RestClient restClient;
    private final CurrencyRepository currencyRepository;
    private final Logger logger = UtilsService.getLogger(this.getClass());

    @PostConstruct
    public void retrieveAndSaveCurrencyValues() {
        CurrencyCode[] codes = CurrencyCode.values();
        logger.debug("Starting fetching currencies");
        for (CurrencyCode code : codes) {
            String name = code.name();
            if (name.equals(CurrencyCode.PLN.name()))
                continue;

            String uri = "http://api.nbp.pl/api/exchangerates/rates/c/%s?format=json".formatted(name);
            CurrencyReceiver currencyReceiver = restClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<CurrencyReceiver>() {
                    });

            RatesReceiver rate = currencyReceiver.rates()[0];
            Currency currencyToSave = Currency.builder()
                    .name(currencyReceiver.name())
                    .code(currencyReceiver.code())
                    .effectiveDate(rate.effectiveDate())
                    .bid(rate.bid())
                    .ask(rate.ask())
                    .build();

            currencyRepository.findByCode(code).ifPresent(
                    currency -> currencyToSave.setId(currency.getId()));

            currencyRepository.save(currencyToSave);
            logger.debug("Saved currency: {}", currencyToSave);
        }
        logger.debug("Currencies fetched and saved");
    }
}
