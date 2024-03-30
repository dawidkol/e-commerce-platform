package pl.dk.ecommerceplatform.currency;

import pl.dk.ecommerceplatform.currency.dtos.CurrencyDto;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDto> getAllCurrencies();

}
