package pl.dk.ecommerceplatform.confirmationToken.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokenDto(Long id, String token, LocalDateTime expiration, Long userId) {
}
