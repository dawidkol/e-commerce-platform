package pl.dk.ecommerceplatform.user.dtos;

import lombok.Builder;

@Builder
public record UserDto(Long id, String firstName, String lastName, String email, String role) {
}
