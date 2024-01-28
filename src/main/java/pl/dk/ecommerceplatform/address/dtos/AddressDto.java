package pl.dk.ecommerceplatform.address.dtos;

import lombok.Builder;

@Builder
public record AddressDto(
        String postalCode,
        String street,
        String buildingNumber,
        String phoneNumber) {
}
