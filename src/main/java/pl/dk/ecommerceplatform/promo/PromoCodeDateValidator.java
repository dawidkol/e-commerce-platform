package pl.dk.ecommerceplatform.promo;

import org.springframework.stereotype.Component;
import pl.dk.ecommerceplatform.promo.dtos.SavePromoDto;

import java.util.function.Predicate;

@Component
class PromoCodeDateValidator implements Predicate<SavePromoDto> {

    @Override
    public boolean test(SavePromoDto savePromoDto) {
        return savePromoDto.activeEnd().isAfter(savePromoDto.activeStart());
    }
}
