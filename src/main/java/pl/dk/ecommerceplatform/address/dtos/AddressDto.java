package pl.dk.ecommerceplatform.address.dtos;

import lombok.Builder;

@Builder
public record AddressDto(
        Long id,
        String postalCode,
        String street,
        String buildingNumber,
        String apartmentNumber,
        String phoneNumber) {
}
