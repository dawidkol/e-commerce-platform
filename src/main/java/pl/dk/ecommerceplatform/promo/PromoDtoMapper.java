package pl.dk.ecommerceplatform.promo;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.promo.dtos.PromoDto;
import pl.dk.ecommerceplatform.promo.dtos.SavePromoDto;

@Service
class PromoDtoMapper {

    public PromoDto map(Promo promo) {
        return PromoDto.builder()
                .id(promo.getId())
                .code(promo.getCode())
                .discountPercent(promo.getDiscountPercent())
                .activeStart(promo.getActiveStart())
                .activeEnd(promo.getActiveEnd())
                .active(promo.getActive())
                .usageCount(promo.getUsageCount())
                .maxUsageCount(promo.getMaxUsageCount())
                .build();
    }

    public Promo map(SavePromoDto savePromoDto) {
        return Promo.builder()
                .code(savePromoDto.code())
                .discountPercent(savePromoDto.discountPercent())
                .activeStart(savePromoDto.activeStart())
                .activeEnd(savePromoDto.activeEnd())
                .active(savePromoDto.active())
                .usageCount(0L)
                .maxUsageCount(savePromoDto.maxUsageCount())
                .build();
    }
}
