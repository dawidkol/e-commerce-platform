package pl.dk.ecommerceplatform.shipping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.dk.ecommerceplatform.error.exceptions.shipping.ShippingMethodExistException;
import pl.dk.ecommerceplatform.error.exceptions.shipping.ShippingNotFoundException;
import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShippingServiceTest {

    @Mock
    private ShippingRepository shippingRepository;

    private ShippingService underTest;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        underTest = new ShippingService(shippingRepository);
    }

    @Test
    void itShouldUpdateShippingCost() {
        // Given
        Long shippingId = 1L;
        BigDecimal newPrice = new BigDecimal("10.00");

        Shipping shippingDHL = Shipping.builder()
                .id(shippingId)
                .name("DHL")
                .shippingCost(BigDecimal.valueOf(10.99))
                .build();

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(shippingDHL));

        // When
        ShippingDto shippingDto = underTest.updateShippingCost(shippingId, newPrice);

        // Then
        verify(shippingRepository, times(1)).findById(shippingId);
        assertThat(shippingDto.shippingCost()).isEqualTo(newPrice);
    }

    @Test
    void itShouldThrowShippingNotFoundExceptionWhenUserWantsToUpdateShippingCost() {
        // Given
        Long shippingId = 1L;
        BigDecimal newPrice = new BigDecimal("10.00");

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ShippingNotFoundException.class, () -> underTest.updateShippingCost(shippingId, newPrice));
        verify(shippingRepository, times(1)).findById(shippingId);
    }

    @Test
    void itShouldSaveNewShippingCost() {
        // Given
        String name = "DHL";
        ShippingDto shippingDtoDHL = ShippingDto.builder()
                .name(name)
                .shippingCost(BigDecimal.valueOf(10.99))
                .build();

        Shipping shippingDHL = Shipping.builder()
                .name(name)
                .shippingCost(shippingDtoDHL.shippingCost())
                .build();

        Shipping savedShippingDHL = Shipping.builder()
                .id(1L)
                .name(name)
                .shippingCost(shippingDtoDHL.shippingCost())
                .build();

        when(shippingRepository.findByName(name)).thenReturn(Optional.empty());
        when(shippingRepository.save(shippingDHL)).thenReturn(savedShippingDHL);

        // When
        ShippingDto shippingDto = underTest.saveShippingMethod(shippingDtoDHL);

        // Then
        ArgumentCaptor<Shipping> shippingArgumentCaptor = ArgumentCaptor.forClass(Shipping.class);
        verify(shippingRepository, times(1)).findByName(name);
        verify(shippingRepository, times(1)).save(shippingArgumentCaptor.capture());
        assertThat(shippingDto.id()).isEqualTo(savedShippingDHL.getId());
    }

    @Test
    void itShouldThrowShippingMethodExistsExceptionWhenUserWantsToSaveNewShippingMethod() {
        // Given
        String name = "DHL";
        ShippingDto shippingDtoDHL = ShippingDto.builder()
                .name(name)
                .shippingCost(BigDecimal.valueOf(10.99))
                .build();

        Shipping shipping = Shipping.builder()
                .id(1L)
                .name(name)
                .shippingCost(shippingDtoDHL.shippingCost())
                .build();

        when(shippingRepository.findByName(name)).thenReturn(Optional.of(shipping));

        // When
        // Then
        assertThrows(ShippingMethodExistException.class, () ->  underTest.saveShippingMethod(shippingDtoDHL));
        verify(shippingRepository, times(1)).findByName(name);

    }
}