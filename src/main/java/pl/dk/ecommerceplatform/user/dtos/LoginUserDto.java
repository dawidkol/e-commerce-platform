package pl.dk.ecommerceplatform.user.dtos;

import lombok.Builder;

@Builder
public record LoginUserDto(String email, String password) {
}
