package pl.dk.ecommerceplatform.security.dtos;

import lombok.Builder;

@Builder
public record UserCredentialsDto(String email, String password, String role, Boolean enabled) {
}
