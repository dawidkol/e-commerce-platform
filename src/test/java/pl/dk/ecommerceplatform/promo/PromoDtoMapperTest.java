package pl.dk.ecommerceplatform.promo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.promo.dtos.PromoDto;
import pl.dk.ecommerceplatform.promo.dtos.SavePromoDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PromoDtoMapperTest {

    private PromoDtoMapper underTest;

    @BeforeEach
    void init() {
        underTest = new PromoDtoMapper();
    }

    @Test
    void mapPromoToPromoDto() {
        // Given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1L);

        Promo promo = Promo.builder()
                .id(1L)
                .code("TestCode")
                .discountPercent(5L)
                .activeStart(LocalDateTime.now())
                .activeEnd(end)
                .active(true)
                .usageCount(1L)
                .maxUsageCount(5L)
                .build();

        // When
        PromoDto promoDto = underTest.map(promo);

        // Then
        assertAll(
                () -> assertThat(promoDto.id()).isEqualTo(promo.getId()),
                () -> assertThat(promoDto.code()).isEqualTo(promo.getCode()),
                () -> assertThat(promoDto.discountPercent()).isEqualTo(promo.getDiscountPercent()),
                () -> assertThat(promoDto.activeStart()).isEqualTo(promo.getActiveStart()),
                () -> assertThat(promoDto.activeEnd()).isEqualTo(promo.getActiveEnd()),
                () -> assertThat(promoDto.active()).isEqualTo(promo.getActive()),
                () -> assertThat(promoDto.usageCount()).isEqualTo(promo.getUsageCount()),
                () -> assertThat(promoDto.maxUsageCount()).isEqualTo(promo.getMaxUsageCount())
        );
    }

    @Test
    void mapSavePromoDtoToPromo() {
        // Given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1L);

        SavePromoDto testcode = SavePromoDto.builder()
                .code("TESTCODE")
                .discountPercent(4L)
                .activeStart(start)
                .activeEnd(end)
                .active(true)
                .maxUsageCount(12L)
                .build();

        // When
        Promo promo = underTest.map(testcode);

        // Then
        assertAll(
                () -> assertThat(promo.getId()).isNull(),
                () -> assertThat(promo.getCode()).isEqualTo(testcode.code()),
                () -> assertThat(promo.getDiscountPercent()).isEqualTo(testcode.discountPercent()),
                () -> assertThat(promo.getActiveStart()).isEqualTo(testcode.activeStart()),
                () -> assertThat(promo.getActiveEnd()).isEqualTo(testcode.activeEnd()),
                () -> assertThat(promo.getActive()).isEqualTo(testcode.active()),
                () -> assertThat(promo.getUsageCount()).isEqualTo(0L),
                () -> assertThat(promo.getMaxUsageCount()).isEqualTo(testcode.maxUsageCount())
        );
    }
}