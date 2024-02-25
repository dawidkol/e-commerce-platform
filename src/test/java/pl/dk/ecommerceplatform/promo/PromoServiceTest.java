package pl.dk.ecommerceplatform.promo;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.dk.ecommerceplatform.promo.dtos.PromoDto;
import pl.dk.ecommerceplatform.promo.dtos.SavePromoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PromoServiceTest {

    @Mock
    private PromoRepository promoRepository;
    @Mock
    private PromoDtoMapper promoDtoMapper;

    private PromoService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new PromoService(promoRepository, promoDtoMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldCreatePromo() {
        // Given
        SavePromoDto savePromoDto = SavePromoDto.builder().build();
        Promo promo = Promo.builder().build();

        when(promoDtoMapper.map(savePromoDto)).thenReturn(promo);
        when(promoRepository.save(promo)).thenReturn(promo);

        // When
        underTest.createPromo(savePromoDto);

        // Then
        verify(promoDtoMapper, times(1)).map(savePromoDto);
        verify(promoRepository, times(1)).save(promo);
    }

    @Test
    void itShouldRetrievePromoByGivenId() {
        // Given
        Long promoId = 1L;
        Promo promo = Promo.builder().build();
        PromoDto promoDto = PromoDto.builder().build();

        when(promoRepository.findById(promoId)).thenReturn(Optional.of(promo));
        when(promoDtoMapper.map(promo)).thenReturn(promoDto);

        // When
        underTest.getPromo(1L);

        // Then
        verify(promoRepository, times(1)).findById(promoId);
        verify(promoDtoMapper, times(1)).map(promo);
    }

    @Test
    void itShouldGenerateRandomPromoCode() {
        // Given
        ArgumentCaptor<Promo> argumentCaptor = ArgumentCaptor.forClass(Promo.class);

        // When
        underTest.generateRandomPromoCode();

        // then
        verify(promoRepository).save(argumentCaptor.capture());
        Promo promo = argumentCaptor.getValue();
        assertAll(
                () -> assertThat(promo.getCode()).isNotNull(),
                () -> assertThat(promo.getDiscountPercent()).isNotNull(),
                () -> assertThat(promo.getActiveStart()).isBefore(promo.getActiveEnd()),
                () -> assertTrue(promo.getActive()),
                () -> assertThat(promo.getUsageCount()).isEqualTo(0L),
                () -> assertThat(promo.getMaxUsageCount()).isEqualTo(100L)
        );
    }

    @Test
    void itShouldGetPromoCodes() {
        // Given
        int page = 0;
        int size = 10;

        List<Promo> promoList = new ArrayList<>();
        promoList.add(new Promo(1L, "CODE1", 10L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), true, 0L, 100L));
        promoList.add(new Promo(2L, "CODE2", 20L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), true, 0L, 100L));

        when(promoRepository.findAllByActiveTrueOrderByIdDesc(PageRequest.of(page, size))).thenReturn(new PageImpl<>(promoList));

        List<PromoDto> expectedPromoDtoList = List.of(
                new PromoDto(1L, "CODE1", 10L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), true, 0L, 100L),
                new PromoDto(2L, "CODE2", 20L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), true, 0L, 100L)
        );
        when(promoDtoMapper.map(promoList.get(0))).thenReturn(expectedPromoDtoList.get(0));
        when(promoDtoMapper.map(promoList.get(1))).thenReturn(expectedPromoDtoList.get(1));

        // When
        List<PromoDto> result = underTest.getPromoCodes(page, size);

        // Then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).isEqualTo(expectedPromoDtoList.get(0)),
                () -> assertThat(result.get(1)).isEqualTo(expectedPromoDtoList.get(1))
        );
    }
}