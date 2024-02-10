package pl.dk.ecommerceplatform.brand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.brand.dtos.BrandDto;
import pl.dk.ecommerceplatform.brand.dtos.SaveBrandDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BrandDtoMapperTest {

    private BrandDtoMapper underTest;

    @BeforeEach
    void init() {
        underTest = new BrandDtoMapper();
    }

    @Test
    void itShouldMapToBrand() {
        // Given
        SaveBrandDto testBrand = SaveBrandDto.builder()
                .name("testBrand")
                .build();

        // When
        Brand brand = underTest.map(testBrand);

        // Then
        assertAll(
                ()-> assertThat(brand.getName()).isEqualTo(testBrand.name())
        );
    }

    @Test
    void itShouldMapToBrandDto() {
        // Given
        Brand testBrand = Brand.builder()
                .id(1L)
                .name("testBrand")
                .build();

        // When
        BrandDto brandDto = underTest.map(testBrand);

        // Then
        assertAll(
                ()-> assertThat(brandDto.id()).isNotNull(),
                ()-> assertThat(brandDto.name()).isEqualTo(testBrand.getName())
        );
    }
}