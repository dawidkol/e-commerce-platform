package pl.dk.ecommerceplatform.currency;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.currency.dtos.CurrencyDto;
import pl.dk.ecommerceplatform.error.exceptions.currency.CurrencyNotFoundException;

import java.util.EnumSet;
import java.util.List;

@Service
@AllArgsConstructor
class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll()
                .stream()
                .map(CurrencyDtoMapper::map)
                .toList();
    }
}
