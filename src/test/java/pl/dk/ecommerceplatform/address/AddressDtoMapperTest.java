package pl.dk.ecommerceplatform.address;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.address.dtos.AddressDto;
import pl.dk.ecommerceplatform.address.dtos.SaveAddressDto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;


class AddressDtoMapperTest {

    private AddressDtoMapper underTest;

    @BeforeEach
    void init() {
        underTest = new AddressDtoMapper();
    }

    @Test
    void itShouldMapToAddressDto() {
        // Given
        Address address = Address.builder()
                .id(1L)
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();

        // When
        AddressDto addressDto = underTest.map(address);

        // Then
        assertAll(
                () -> assertThat(addressDto.id()).isEqualTo(address.getId()),
                () -> assertThat(addressDto.postalCode()).isEqualTo(address.getPostalCode()),
                () -> assertThat(addressDto.street()).isEqualTo(address.getStreet()),
                () -> assertThat(addressDto.buildingNumber()).isEqualTo(address.getBuildingNumber()),
                () -> assertThat(addressDto.phoneNumber()).isEqualTo(address.getPhoneNumber())

        );
    }

    @Test
    void itShouldMapToAddress() {
        // Given
        SaveAddressDto saveAddressDto = SaveAddressDto.builder()
                .id(1L)
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();


        // When
        Address address = underTest.map(saveAddressDto);

        // Then
        assertAll(
                () -> assertThat(address.getId()).isEqualTo(saveAddressDto.id()),
                () -> assertThat(address.getPostalCode()).isEqualTo(saveAddressDto.postalCode()),
                () -> assertThat(address.getStreet()).isEqualTo(saveAddressDto.street()),
                () -> assertThat(address.getBuildingNumber()).isEqualTo(saveAddressDto.buildingNumber()),
                () -> assertThat(address.getPhoneNumber()).isEqualTo(saveAddressDto.phoneNumber())

        );
    }
}