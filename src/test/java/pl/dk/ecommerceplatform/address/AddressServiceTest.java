package pl.dk.ecommerceplatform.address;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;
import pl.dk.ecommerceplatform.address.dtos.AddressDto;
import pl.dk.ecommerceplatform.address.dtos.SaveAddressDto;
import pl.dk.ecommerceplatform.error.exceptions.address.AddressNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.address.UpdateAddressException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock
    private AddressDtoMapper addressDtoMapper;
    @Mock
    private AddressRepository addressRepository;
    private AddressService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AddressService(addressRepository, addressDtoMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();;
    }

    @Test
    void itShouldCreateShippingAddress() {
        // Given
        SaveAddressDto saveAddressDto = SaveAddressDto.builder()
                .id(1L)
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();

        Address address = Address.builder()
                .id(1L)
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();

        AddressDto expected = AddressDto.builder()
                .id(1L)
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();

        when(addressDtoMapper.map(saveAddressDto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressDtoMapper.map(address)).thenReturn(expected);

        // When
        underTest.createShippingAddress(saveAddressDto);

        // Then
        verify(addressDtoMapper, times(1)).map(saveAddressDto);
        verify(addressRepository, times(1)).save(address);
        verify(addressDtoMapper, times(1)).map(address);
    }

    @Test
    void itShouldUpdateShippingAddress() {
        // Given
        SaveAddressDto saveAddressDto = SaveAddressDto.builder()
                .id(1L)
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();

        Address address = Address.builder()
                .id(1L)
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();

        when(addressRepository.findById(saveAddressDto.id())).thenReturn(Optional.of(address));
        when(addressDtoMapper.map(saveAddressDto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);

        // When
        underTest.updateShippingAddress(saveAddressDto);

        // Then
        verify(addressDtoMapper, times(1)).map(saveAddressDto);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void itShouldThrowUpdateAddressException() {
        // Given
        SaveAddressDto saveAddressDto = SaveAddressDto.builder()
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();

        // When
        // Then
        assertThrows(UpdateAddressException.class, () -> underTest.updateShippingAddress(saveAddressDto));
    }

    @Test
    void itShouldThrowAddressNotFoundException() {
        // Given
        SaveAddressDto saveAddressDto = SaveAddressDto.builder()
                .id(1L)
                .postalCode("22-400")
                .street("testowa ulica")
                .buildingNumber("13A")
                .phoneNumber("666666666")
                .build();

        when(addressRepository.findById(saveAddressDto.id())).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(AddressNotFoundException.class, () -> underTest.updateShippingAddress(saveAddressDto));
        verify(addressRepository, times(1)).findById(saveAddressDto.id());
    }

}