package pl.dk.ecommerceplatform.promo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pl.dk.ecommerceplatform.error.exceptions.promo.InvalidDateException;
import pl.dk.ecommerceplatform.error.exceptions.promo.PromoCodeNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.promo.dtos.PromoDto;
import pl.dk.ecommerceplatform.promo.dtos.SavePromoDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.time.LocalDateTime;
import java.util.List;
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
    private UtilsService utilsService;
    private PromoCodeDateValidator dateValidator;
    private final Logger logger = getLogger(this.getClass());

    @Transactional
    public PromoDto createPromo(SavePromoDto savePromoDto) {
        boolean dateValidation = dateValidator.test(savePromoDto);
        if (!dateValidation) {
            throw new InvalidDateException("Promo code activeStart date [%s] is after promo code activeEnd date [%s]"
                    .formatted(savePromoDto.activeStart(), savePromoDto.activeEnd()));
        }
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

    public List<PromoDto> getPromoCodes(int page, int size) {
        return promoRepository.findAllByActiveTrueOrderByIdDesc(PageRequest.of(page, size))
                .stream()
                .map(promoDtoMapper::map)
                .toList();
    }

    public void updatePromoCode(Long id, JsonMergePatch jsonMergePatch) {
        Promo promoCodeToUpdate = promoRepository.findById(id).orElseThrow(PromoCodeNotFoundException::new);
        try {
            SavePromoDto savePromoDto = utilsService.applyPatch(promoCodeToUpdate, jsonMergePatch, SavePromoDto.class);
            boolean dateValidation = dateValidator.test(savePromoDto);
            if (!dateValidation) {
                throw new InvalidDateException("Promo code activeStart date [%s] is after promo code activeEnd date [%s]"
                        .formatted(savePromoDto.activeStart(), savePromoDto.activeEnd()));
            }
            Promo promoCodeToSave = promoDtoMapper.map(savePromoDto);
            promoRepository.save(promoCodeToSave);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
    }
}
