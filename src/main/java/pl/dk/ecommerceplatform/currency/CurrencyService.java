package pl.dk.ecommerceplatform.currency;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.currency.dtos.CurrencyDto;

import java.util.List;

@Service
@AllArgsConstructor
class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll()
                .stream()
                .map(CurrencyDtoMapper::map)
                .toList();
    }
}
