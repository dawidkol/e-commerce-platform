package pl.dk.ecommerceplatform.shipping;

import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ShippingDtoMapperTest {

    @Test
    void itShouldMapShippingDtoToShipping() {
        // Given
        Shipping shippingDHL = Shipping.builder()
                .id(1L)
                .name("DHL")
                .shippingCost(BigDecimal.valueOf(10.99))
                .build();

        // When
        ShippingDto shippingDto = ShippingDtoMapper.map(shippingDHL);

        // Then
        assertAll(
                () -> assertThat(shippingDto.id()).isEqualTo(shippingDHL.getId()),
                () -> assertThat(shippingDto.name()).isEqualTo(shippingDHL.getName()),
                () -> assertThat(shippingDto.shippingCost()).isEqualTo(shippingDHL.getShippingCost())
        );
    }

    @Test
    void itShouldMapShippingToShippingDto() {
        // Given
        ShippingDto shippingDtoDHL = ShippingDto.builder()
                .id(1L)
                .name("DHL")
                .shippingCost(BigDecimal.valueOf(10.99))
                .build();

        // When
        Shipping shipping = ShippingDtoMapper.map(shippingDtoDHL);

        // Then
        assertAll(
                () -> assertThat(shipping.getId()).isEqualTo(shippingDtoDHL.id()),
                () -> assertThat(shipping.getName()).isEqualTo(shippingDtoDHL.name()),
                () -> assertThat(shipping.getShippingCost()).isEqualTo(shippingDtoDHL.shippingCost())
        );

    }

}