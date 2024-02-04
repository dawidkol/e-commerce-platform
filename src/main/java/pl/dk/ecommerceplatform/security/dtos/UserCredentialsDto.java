package pl.dk.ecommerceplatform.user.dtos;

import lombok.Builder;

@Builder
public record UserCredentialsDto(String email, String password, String role) {
}
