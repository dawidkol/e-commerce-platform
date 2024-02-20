package pl.dk.ecommerceplatform.promo;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.promo.PromoCodeNotFoundException;
import pl.dk.ecommerceplatform.promo.dtos.PromoDto;
import pl.dk.ecommerceplatform.promo.dtos.SavePromoDto;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static pl.dk.ecommerceplatform.utils.UtilsService.getLogger;

@Service
@AllArgsConstructor
@EnableAsync
class PromoService {

    private PromoRepository promoRepository;
    private PromoDtoMapper promoDtoMapper;
    private final Logger logger = getLogger(this.getClass());

    @Transactional
    public PromoDto createPromo(SavePromoDto savePromoDto) {
        Promo promoToSave = promoDtoMapper.map(savePromoDto);
        Promo savedPromo = promoRepository.save(promoToSave);
        return promoDtoMapper.map(savedPromo);
    }

    public PromoDto getPromo(Long id) {
        Promo promo = promoRepository.findById(id).orElseThrow(PromoCodeNotFoundException::new);
        return promoDtoMapper.map(promo);
    }

    @Async
    @Scheduled(cron = "${scheduler.promo}")
    @Transactional
    public void generateRandomPromoCode() {
        Promo randomPromoCode = this.createRandomPromoCode();
        promoRepository.save(randomPromoCode);
        logger.warn("Random promo code generated. Code = {} ", randomPromoCode.getCode());
    }

    private Promo createRandomPromoCode() {
        String code = UUID.randomUUID().toString();
        Random random = new Random();
        int[] discounts = IntStream.rangeClosed(5, 36).toArray();
        LocalDateTime activeStart = LocalDateTime.now();
        LocalDateTime activeEnd = activeStart.plusDays(1L);
        int discount = discounts[(random.nextInt(0, discounts.length))];
        return Promo.builder()
                .code(code)
                .discountPercent(random.nextLong(discount))
                .activeStart(activeStart)
                .activeEnd(activeEnd)
                .active(true)
                .usageCount(0L)
                .maxUsageCount(100L)
                .build();
    }
}
