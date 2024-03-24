package pl.dk.ecommerceplatform.currency;

import pl.dk.ecommerceplatform.currency.dtos.CurrencyDto;

class CurrencyDtoMapper {

    public static CurrencyDto map(Currency currency) {
        return CurrencyDto.builder()
                .id(currency.getId())
                .name(currency.getName())
                .code(currency.getCode().name())
                .effectiveDate(currency.getEffectiveDate())
                .bid(currency.getBid())
                .ask(currency.getAsk())
                .build();
    }
}
