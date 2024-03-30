package pl.dk.ecommerceplatform.currency;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dk.ecommerceplatform.currency.dtos.CurrencyDto;

import java.util.List;

@RestController
@RequestMapping("/currency")
@AllArgsConstructor
class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("")
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencies);
    }

}
