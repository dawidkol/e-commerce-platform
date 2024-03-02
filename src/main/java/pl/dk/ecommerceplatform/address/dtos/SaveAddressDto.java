package pl.dk.ecommerceplatform.address.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SaveAddressDto(
        Long id,
        @NotBlank String postalCode,
        @NotBlank String street,
        @NotBlank String buildingNumber,
        @NotBlank String phoneNumber) {
}
